package com.example.publicoffering.scheduler;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

@Data
class PublicOfferLists{
    private List<PublicOffer> listOneDay;
    private List<PublicOffer> listThreeDay;

    public PublicOfferLists(List<PublicOffer> publicOfferOneDay, List<PublicOffer> publicOfferThreeDay) {
        listOneDay = publicOfferOneDay;
        listThreeDay = publicOfferThreeDay;
    }
}

@Component
public class CrawlingScheduler {

    @Scheduled(cron = "1 * * * * *")
    public void method() {
        PublicOfferLists publicOfferLists = Crawling();
        if (publicOfferLists != null) {
            sendMsg(publicOfferLists);
        }
    }

    private PublicOfferLists Crawling() {
        List<PublicOffer> publicOfferOneDay = new ArrayList<>();
        List<PublicOffer> publicOfferThreeDay = new ArrayList<>();

        try {
            //Crawling!
            String publicOfferingUrl = "http://www.ipostock.co.kr/sub03/ipo04.asp";
            Document doc = Jsoup.connect(publicOfferingUrl).get();
            Element tableElement = doc.selectXpath("//*[@id='print']/table[1]/tbody/tr[4]/td/table/tbody/tr[4]/td/table").first();

            if (tableElement == null) {
                throw new NullPointerException("크롤링 결과 없음");
            }

            LocalDate today = LocalDate.now();
            Elements rows = tableElement.select("tr");

            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() <= 1) {
                    continue;
                }

                String date = cells.get(1).text().split("~")[0].trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
                LocalDate givenDate = LocalDate.parse(date + "." + today.getYear(), DateTimeFormatter.ofPattern("MM.dd.yyyy"));
                
                if (givenDate.equals(today)){
                    publicOfferOneDay.add(getOfferFromCells(cells));
                }
                else if(givenDate.equals(today.plusDays(3))){
                    publicOfferThreeDay.add(getOfferFromCells(cells));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new PublicOfferLists(publicOfferOneDay, publicOfferThreeDay);
    }
    private void sendMsg(PublicOfferLists publicOfferLists) {
        System.out.println("hi");

        for(PublicOffer publicOffer : publicOfferLists.getListThreeDay()){
            System.out.println("publicOffer = " + publicOffer);
        }
        for (PublicOffer publicOffer: publicOfferLists.getListOneDay()){
            System.out.println("publicOffer = " + publicOffer);
        }
    }

    private PublicOffer getOfferFromCells(Elements cells){
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
        return publicOffer;
    }
}
