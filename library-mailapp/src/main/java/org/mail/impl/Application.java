/*
package org.mail.impl;

*/
/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;*//*


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Application {

    */
/* private static final Logger log = LoggerFactory.getLogger(Application.class);*//*


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("ploc");
        */
/*log.info("The time is now {}", dateFormat.format(new Date()));*//*

    }
}*/
