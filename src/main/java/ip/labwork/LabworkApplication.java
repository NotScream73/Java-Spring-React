package ip.labwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LabworkApplication {
	public static void main(String[] args) {
		SpringApplication.run(LabworkApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/sum")
	public String Sum(@RequestParam(defaultValue = "0") double first,
					  @RequestParam(defaultValue = "0") double second) {
		return Double.toString(first + second);
	}

	@GetMapping("/minus")
	public String Ras(@RequestParam(defaultValue = "0") double first,
					  @RequestParam(defaultValue = "0") double second) {
		return Double.toString(first - second);
	}

	@GetMapping("/multi")
	public String Pros(@RequestParam(defaultValue = "1") double first,
					   @RequestParam(defaultValue = "1") double second) {
		return Double.toString(first * second);
	}

	@GetMapping("/div")
	public String Del(@RequestParam(defaultValue = "1") double first,
					  @RequestParam(defaultValue = "1") double second) {
		if (second == 0) {
			return null;
		}
		return Double.toString(first / second);
	}
}
