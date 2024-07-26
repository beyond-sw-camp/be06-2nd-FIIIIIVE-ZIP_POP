package com.fiiiiive.zippop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZippopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZippopApplication.class, args);
	}

}
