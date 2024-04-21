package com.example.publicoffering.scheduler;

import com.example.publicoffering.model.PublicOfferLists;
import com.example.publicoffering.service.KakaoService;
import com.example.publicoffering.service.PublicOfferService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlingScheduler {
    private final PublicOfferService publicOfferService;
    private final KakaoService kakaoService;


    public CrawlingScheduler() {
        publicOfferService = new PublicOfferService();
        kakaoService = new KakaoService();
    }

    @Scheduled(cron = "1 * * * * *")
    public void method() {
        PublicOfferLists publicOfferLists = publicOfferService.Crawling();
        kakaoService.sendMsg(publicOfferLists);
    }


}
