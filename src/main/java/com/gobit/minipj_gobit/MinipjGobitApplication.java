package com.gobit.minipj_gobit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MinipjGobitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinipjGobitApplication.class, args);
    }

}
