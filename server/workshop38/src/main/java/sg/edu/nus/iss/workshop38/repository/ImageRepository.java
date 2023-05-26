package sg.edu.nus.iss.workshop38.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
import sg.edu.nus.iss.workshop38.model.Image;
import sg.edu.nus.iss.workshop38.model.UserImage;

import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static sg.edu.nus.iss.workshop38.repository.DBQueries.*;

@Repository
public class ImageRepository {

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private RedisTemplate redis;

    // S3
    public String uploadImage(String comments, MultipartFile file) throws IOException {
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
        return key;
    }

    // S3
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

    // MONGO
    public void insertImageUser(String username, String photoKey, String comments) {

        Document existingUserDoc = getUserImage(username);

        if (existingUserDoc == null) {
            Image image = new Image(photoKey, comments);
            List<Image> imageList = new ArrayList<>();
            imageList.add(image);
            UserImage userImage = new UserImage(username, imageList);
            Document d = Document.parse(userImage.toJsonObject().toString());
            mongo.insert(d, "user");
            return;
        }

        UserImage userImage = UserImage.convertFromDocument(existingUserDoc);
        Image newImg = new Image(photoKey, comments);
        List<Image> imgList = userImage.getImages();
        List<Image> newImgList = new ArrayList<>();

        for (Image img : imgList) {
            newImgList.add(img);
        }
        newImgList.add(newImg);
        userImage.setImages(newImgList);

        List<Document> newImgDocumentList = newImgList.stream()
                .map(img -> Document.parse(img.toJsonObjectBuilder().build().toString()))
                .toList();

        Query q = Query.query(Criteria.where("username").is(username));

        Update updateOps = new Update()
                .set("username", username)
                .set("images", newImgDocumentList);

        mongo.updateMulti(q, updateOps, Document.class, "user");
        return;
    }

    // MONGO
    public Document getUserImage(String username) {
        Query q = Query.query(Criteria.where("username").is(username));
        return mongo.findOne(q, Document.class, "user");
    }

    // SQL
    public boolean verifyUser(String username, String password) {
        SqlRowSet rs = jdbc.queryForRowSet(SQL_VERIFY_USER, username, password);
        if (rs.next()) {
            return true;
        }
        return false;
    }

}
