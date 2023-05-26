package sg.edu.nus.iss.workshop38.service;

import java.io.IOException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop38.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imgRepo;

    public URL uploadImage(String comments, MultipartFile file) throws IOException {
        return imgRepo.uploadImage(comments, file);
    }

    public ResponseEntity<String> getImages(String key) throws IOException {
        return imgRepo.getImages(key);
    }

}
