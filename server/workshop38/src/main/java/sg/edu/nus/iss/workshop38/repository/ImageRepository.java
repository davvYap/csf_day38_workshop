package sg.edu.nus.iss.workshop38.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

@Repository
public class ImageRepository {

    @Autowired
    private AmazonS3 s3;

    public URL uploadImage(String comments, MultipartFile file) throws IOException {
        // construct custom MetaData
        Map<String, String> userData = new HashMap<>();
        userData.put("comments", comments);
        userData.put("filename", file.getOriginalFilename());
        userData.put("date_inserted", new Date().toString());

        // construct Object Metadata
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(file.getContentType());
        metaData.setContentLength(file.getSize());
        metaData.setUserMetadata(userData);

        // Generate unique key
        String key = UUID.randomUUID().toString().substring(0, 8);

        // Put into S3 Bucket
        PutObjectRequest putReq = new PutObjectRequest("ddavv",
                "images/%s".formatted(key),
                file.getInputStream(),
                metaData);
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putReq);
        return s3.getUrl("ddavv", key);
    }
}
