package com.shift102;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.shift102"})
@EnableTransactionManagement
public class Application {
	@Autowired
    JdbcTemplate jdbcTemplate;
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
