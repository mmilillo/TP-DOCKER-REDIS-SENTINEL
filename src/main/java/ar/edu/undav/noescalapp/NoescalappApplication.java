package ar.edu.undav.noescalapp;

import ar.edu.undav.noescalapp.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SpringConfig.class)
public class NoescalappApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoescalappApplication.class, args);
	}

}
