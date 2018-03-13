package com.kakaopay.fds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kakaopay.fds.foundation.FDSConfig;

@SpringBootApplication
public class FdsApplication {

	public static void main(String[] args) throws Exception {
		FDSConfig.loadProperties("FDS.properties");
		SpringApplication.run(FdsApplication.class, args);
	}

}
