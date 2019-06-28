package org.mail.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduledTasks {


    public static void main(String[] args) {
        SpringApplication.run(ScheduledTasks.class, args);
    }
}
