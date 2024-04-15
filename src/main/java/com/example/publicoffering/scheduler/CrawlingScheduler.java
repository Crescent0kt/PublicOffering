package com.example.publicoffering.scheduler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlingScheduler {
    private final String publicOfferingUrl = "http://www.ipostock.co.kr/sub03/ipo04.asp";
    @Scheduled(cron = "1 * * * * *")
    public void Crawling(){
        try{
            Document doc = Jsoup.connect(publicOfferingUrl).get();
            Element tableElement = doc.selectXpath("//*[@id='print']/table[1]/tbody/tr[4]/td/table/tbody/tr[4]/td/table").first();
            assert tableElement != null;
            Elements rows = tableElement.select("tr");

            for (Element row : rows) {
                Elements cells = row.select("td");
                for (Element cell : cells) {

                    System.out.println(cell.text());
                }
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }



    }
}
