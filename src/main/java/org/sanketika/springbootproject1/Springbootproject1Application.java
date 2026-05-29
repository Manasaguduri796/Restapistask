package org.sanketika.springbootproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
@EnableKafka

@SpringBootApplication
public class Springbootproject1Application {

	public static void main(String[] args) {
		SpringApplication.run(Springbootproject1Application.class, args);
	}

}
