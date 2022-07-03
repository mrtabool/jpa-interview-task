package com.example.jpademo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class JpademoApplication {
	private static final Logger log = LoggerFactory.getLogger(JpademoApplication.class);

	public static void main(String[] args) {
		var env = SpringApplication.run(JpademoApplication.class, args).getEnvironment();
		log.info(
				"----------------------------------------------------------\n" +
						"\tApplication '{}' is running!\n" +
						"\tAccess URL: http://localhost:{}\n" +
						"\tSwagger UI: http://localhost:{}/swagger-ui.html\n" +
						"\tProfile(s): {}\n" +
						"----------------------------------------------------------\n",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port"),
				env.getProperty("server.port"),
				env.getActiveProfiles());
	}

}
