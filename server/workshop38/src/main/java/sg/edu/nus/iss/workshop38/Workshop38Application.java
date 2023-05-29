package sg.edu.nus.iss.workshop38;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Workshop38Application {

	public static void main(String[] args) {
		SpringApplication.run(Workshop38Application.class, args);
	}

}
