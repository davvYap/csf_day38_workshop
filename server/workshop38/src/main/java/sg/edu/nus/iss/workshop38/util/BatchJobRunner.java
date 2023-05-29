package sg.edu.nus.iss.workshop38.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.workshop38.service.ImageService;

@Component
public class BatchJobRunner implements CommandLineRunner {

    @Autowired
    private ImageService imgSvc;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Batch Job started ------ Redis Keys below");
        List<String> redisKeys = imgSvc.getImageKeysFromRedis();
        for (String key : redisKeys) {
            System.out.println(key);
        }
    }

}
