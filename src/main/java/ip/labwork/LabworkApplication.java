package ip.labwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LabworkApplication {
	public static void main(String[] args) {
		SpringApplication.run(LabworkApplication.class, args);
	}
}
