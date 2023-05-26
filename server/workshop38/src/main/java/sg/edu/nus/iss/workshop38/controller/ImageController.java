package sg.edu.nus.iss.workshop38.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop38.model.UserImage;
import sg.edu.nus.iss.workshop38.service.ImageService;

@Controller
@RequestMapping
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imgSvc;

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadImage(@RequestPart String comments, @RequestPart MultipartFile file,
            @RequestPart String username) {
        try {
            String key = imgSvc.uploadImage(comments, file);
            imgSvc.insertImageUser(username, key, comments);
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

    @GetMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        boolean isLogin = imgSvc.verifyUser(username, password);
        JsonObject res = Json.createObjectBuilder().add("login", isLogin).build();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(res.toString());
    }

    @GetMapping(path = "/userImage")
    public ResponseEntity<String> getUserImage(@RequestParam String username) {

        UserImage userImage = imgSvc.getUserImage(username);

        if (userImage == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("username", "not found")
                            .build().toString());
        }

        JsonObject result = userImage.toJsonObject();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

}
