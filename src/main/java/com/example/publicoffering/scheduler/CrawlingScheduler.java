package com.example.publicoffering.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlingScheduler {
    @Scheduled(cron = "1 * * * * *")
    public void Crawling(){
        System.out.println("test");

    }
}
