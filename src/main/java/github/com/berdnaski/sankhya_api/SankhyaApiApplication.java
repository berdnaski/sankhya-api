package github.com.berdnaski.sankhya_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SankhyaApiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SankhyaApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SankhyaApiApplication.class, args);
	}

}
