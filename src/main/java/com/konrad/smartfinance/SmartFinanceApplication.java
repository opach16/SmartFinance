package com.konrad.smartfinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFinanceApplication.class, args);
    }

}
