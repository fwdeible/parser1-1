package com.project.parser1;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Parser11Application {

	static Logger log = Logger.getLogger(Parser11Application.class.getName());
	
	public static void main(String[] args) {

		log.debug("Application main");
		
		//startup and handles command line args
		ApplicationContext context = SpringApplication.run(Parser11Application.class, args);

		//Application will terminate automatically after runtime unless told otherwise
		if (!Arrays.asList(args).contains("keepAlive")) {
			SpringApplication.exit(context);
		}

	}
}
