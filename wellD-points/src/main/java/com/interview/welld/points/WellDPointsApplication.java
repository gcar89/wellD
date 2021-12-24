package com.interview.welld.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.interview.welld.points.controller")
@SpringBootApplication
public class WellDPointsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WellDPointsApplication.class, args);
	}

}
