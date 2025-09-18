package top.sxmeng.week1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class QuickstartApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuickstartApplication.class, args);
	}

//	@GetMapping("/hello")
//	public String hello() {
//		return "Hello World";
//	}
}
