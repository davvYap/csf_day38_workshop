package sg.edu.nus.iss.workshop38.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Repository
public class ImageRepository {

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private RedisTemplate redis;

    public URL uploadImage(String comments, MultipartFile file) throws IOException {
        // construct custom MetaData
        Map<String, String> userdata = new HashMap<>();
        userdata.put("comments", comments);
        userdata.put("filename", file.getOriginalFilename());
        userdata.put("date_inserted", new Date().toString());

        // construct Object Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userdata);

        // Generate unique key
        String key = UUID.randomUUID().toString().substring(0, 8);

        // Put into S3 Bucket
        PutObjectRequest putReq = new PutObjectRequest("ddavv",
                "images/%s".formatted(key),
                file.getInputStream(),
                metadata);
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putReq);
        return s3.getUrl("ddavv", key);
    }

    public ResponseEntity<String> getImages(String key) throws IOException {

        try {
            GetObjectRequest getReq = new GetObjectRequest("ddavv", key);
            S3Object result = s3.getObject(getReq);
            ObjectMetadata metadata = result.getObjectMetadata();
            Map<String, String> userdata = metadata.getUserMetadata();
            try (S3ObjectInputStream is = result.getObjectContent()) {
                byte[] image = is.readAllBytes();
                String base64 = convertImageToBase64(image);
                // System.out.println("Content Type >>> " + metadata.getContentType());
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        // .contentLength(metadata.getContentLength())
                        .header("X-name", userdata.get("filename"))
                        .body(convertToJson(base64).toString());
            } catch (AmazonS3Exception ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(convertToJson("NOT FOUND").toString());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(convertToJson("NOT FOUND").toString());
        }

    }

    public String convertImageToBase64(byte[] image) {
        String base64 = Base64.getEncoder().encodeToString(image);
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,").append(base64);
        return sb.toString();
    }

    public JsonObject convertToJson(String base64) {
        return Json.createObjectBuilder()
                .add("image", base64)
                .build();
    }
}
