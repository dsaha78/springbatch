package io.spring.batch.ffinput;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchflatfileinputreaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchflatfileinputreaderApplication.class, args);
	}
}
