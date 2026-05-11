package com.chat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the chat application.
 */
@SpringBootApplication
public class AppApplication {

	/**
	 * Boots the Spring application context.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
