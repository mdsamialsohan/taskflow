package com.samialsohan.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //it shortcut of three things ; @Configure, @EnableAutoConfiguration, @ComponentScan.

//@Configuration — "this class can define beans (objects that Spring manages)"
//@EnableAutoConfiguration — "Spring, look at my dependencies and configure things automatically." Because you added spring-boot-starter-web, Spring auto-configures an embedded Tomcat web server. Because you added spring-data-jpa, it auto-configures Hibernate. You didn't write any of that setup code — Spring figured it out from your pom.xml.
//@ComponentScan — "scan every class in com.taskflow and its sub-packages. If you find @Controller, @Service, or @Repository, create an instance and manage it."

public class TaskflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}

}
