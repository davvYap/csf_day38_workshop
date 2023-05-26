package sg.edu.nus.iss.workshop38.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import sg.edu.nus.iss.workshop38.service.ImageService;

@Controller
@RequestMapping
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imgSvc;

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadImage(@RequestPart String comments, @RequestPart MultipartFile file) {
        try {
            URL url = imgSvc.uploadImage(comments, file);
            System.out.println("URL from S3 >>> " + url);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(Json.createObjectBuilder().add("Error", e.getMessage()).build().toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(Json.createObjectBuilder().add("Upload", "Successful").build().toString());
    }

    @GetMapping(path = "/image")
    public ResponseEntity<String> getImage(@RequestParam String key) throws IOException {
        String bucketKey = "images/%s".formatted(key);
        System.out.println("json >>> " + imgSvc.getImages(bucketKey).getBody());
        return imgSvc.getImages(bucketKey);
    }
}
