package ca.mcgill.ecse321.rideshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class RideShareApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideShareApplication.class, args);
	}
	
	@RequestMapping("/")
	public String greeting(){
		return "Hello world!";
	}
}
