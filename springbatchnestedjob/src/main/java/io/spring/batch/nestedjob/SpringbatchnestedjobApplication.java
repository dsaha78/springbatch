package io.spring.batch.nestedjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchnestedjobApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchnestedjobApplication.class, args);
	}
}
