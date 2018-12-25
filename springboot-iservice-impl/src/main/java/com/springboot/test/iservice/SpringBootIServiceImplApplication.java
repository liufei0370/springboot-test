package com.springboot.test.iservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.springboot.test.mapper")
@SpringBootApplication
public class SpringBootIServiceImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootIServiceImplApplication.class, args);
	}

}

