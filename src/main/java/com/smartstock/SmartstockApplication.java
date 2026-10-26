package com.smartstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartstockApplication {

    public static void main(String[] args) {
        System.out.println(
                "DB_URL present: " +
                        (System.getenv("DB_URL") != null &&
                                !System.getenv("DB_URL").isBlank())
        );

        SpringApplication.run(
                SmartstockApplication.class, args
        );
    }
}