package com.vits.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BookingManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingManagerApplication.class, args);
    }
}
