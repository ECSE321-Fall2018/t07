package ca.mcgill.ecse321.eventregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ecse321BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ecse321BackendApplication.class, args);
	}
	
	public String Greetings() {
		return "Hello World!";
	}
}
