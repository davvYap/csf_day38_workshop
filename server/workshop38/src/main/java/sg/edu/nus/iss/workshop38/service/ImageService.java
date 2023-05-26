package sg.edu.nus.iss.workshop38.service;

import java.io.IOException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop38.model.UserImage;
import sg.edu.nus.iss.workshop38.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imgRepo;

    public String uploadImage(String comments, MultipartFile file) throws IOException {
        return imgRepo.uploadImage(comments, file);
    }

    public ResponseEntity<String> getImages(String key) throws IOException {
        return imgRepo.getImages(key);
    }

    public void insertImageUser(String username, String photoKey, String comments) {
        imgRepo.insertImageUser(username, photoKey, comments);
    }

    public boolean verifyUser(String username, String password) {
        return imgRepo.verifyUser(username, password);
    }

    public UserImage getUserImage(String username) {
        Document d = imgRepo.getUserImage(username);
        if (d == null || d.isEmpty()) {
            return null;
        }
        UserImage userImage = UserImage.convertFromDocument(d);
        return userImage;
    }

}
