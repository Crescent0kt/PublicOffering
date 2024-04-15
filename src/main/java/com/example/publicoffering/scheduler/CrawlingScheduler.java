package com.example.publicoffering.scheduler;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
class PublicOffer{
    String scheduleDate;
    String itemName;
    String desiredPrice;
    String offeringPrice;
    String offeringAmount;
    String refundDate;
    String listDate;
    String competitionRatio;
    String leadManager;


}
@Component
public class CrawlingScheduler {


    @Scheduled(cron = "1 * * * * *")
    public void method(){
        List<PublicOffer> publicOfferList = Crawling();
    }

    private List<PublicOffer> Crawling(){
        List<PublicOffer> publicOfferList = new ArrayList<>();
        try{
            String publicOfferingUrl = "http://www.ipostock.co.kr/sub03/ipo04.asp";
            Document doc = Jsoup.connect(publicOfferingUrl).get();
            Element tableElement = doc.selectXpath("//*[@id='print']/table[1]/tbody/tr[4]/td/table/tbody/tr[4]/td/table").first();

            if(tableElement == null){
                return null;
            }

            Elements rows = tableElement.select("tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() > 1){
                    PublicOffer publicOffer = new PublicOffer();

                }
                for (Element cell : cells) {
                    System.out.println(cell.text());
                }
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return publicOfferList;

    }
}
