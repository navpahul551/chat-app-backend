package com.nav.ChatApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApiApplication.class, args);
	}

}
