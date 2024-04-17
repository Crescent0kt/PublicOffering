package com.example.publicoffering.scheduler;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
class PublicOffer {
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

    @Scheduled(cron = "0 0 8 * * *")
    public void method() {
        List<PublicOffer> publicOfferList = Crawling();
        if (publicOfferList != null) {
            sendMsg(publicOfferList);
        }
    }

    private List<PublicOffer> Crawling() {
        List<PublicOffer> publicOfferList = new ArrayList<>();
        try {
            String publicOfferingUrl = "http://www.ipostock.co.kr/sub03/ipo04.asp";
            Document doc = Jsoup.connect(publicOfferingUrl).get();
            Element tableElement = doc.selectXpath("//*[@id='print']/table[1]/tbody/tr[4]/td/table/tbody/tr[4]/td/table").first();
            LocalDate today = LocalDate.now();

            if (tableElement == null) {
                return null;
            }

            Elements rows = tableElement.select("tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() <= 1) {
                    continue;
                }

                PublicOffer publicOffer = new PublicOffer();
                publicOffer.setScheduleDate(cells.get(1).text());
                publicOffer.setItemName(cells.get(2).text());
                publicOffer.setDesiredPrice(cells.get(3).text());
                publicOffer.setOfferingPrice(cells.get(4).text());
                publicOffer.setOfferingAmount(cells.get(5).text());
                publicOffer.setRefundDate(cells.get(6).text());
                publicOffer.setListDate(cells.get(7).text());
                publicOffer.setCompetitionRatio(cells.get(8).text());
                publicOffer.setLeadManager(cells.get(9).text());
                publicOfferList.add(publicOffer);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return publicOfferList;
    }
    private void sendMsg(List<PublicOffer> publicOfferList) {
        for (PublicOffer po : publicOfferList) {
            System.out.println(po);
        }
    }
}
