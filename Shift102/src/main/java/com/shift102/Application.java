package com.shift102;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.shift102")
@EntityScan("com.shift102")
@EnableTransactionManagement
public class Application {
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
