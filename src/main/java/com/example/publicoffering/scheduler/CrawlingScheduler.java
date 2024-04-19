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
import java.util.Objects;

@Data
class PublicOffer {
    String itemName;
    String offeringPrice;
    String leadManager;
    String sector;
    String scheduledDate;
    String listingDate;
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
        sendMsg(publicOfferLists);
    }

    private PublicOfferLists Crawling() {
        List<PublicOffer> publicOfferOneDay = new ArrayList<>();
        List<PublicOffer> publicOfferThreeDay = new ArrayList<>();

        try {
            //Crawling!
            String publicOfferingUrl = "https://finance.naver.com/sise/ipo.naver";
            Document doc = Jsoup.connect(publicOfferingUrl).get();
            Element tableElement = doc.selectXpath("//*[@id=\"contentarea\"]/div[2]/table/tbody").first();

            if (tableElement == null) {
                throw new NullPointerException("크롤링 결과 없음");
            }

            LocalDate today = LocalDate.now();
            Elements rows = tableElement.select("tr");

            for (Element row : rows) {
                Element date = row.select("li.area_private span.num").first();
                if(date == null){
                    continue;
                }
                String dateStr = date.text().split("~")[0].trim();
                LocalDate givenDate = LocalDate.parse(dateStr,DateTimeFormatter.ofPattern("yy.MM.dd"));
                System.out.println("givenDate = " + givenDate);
                System.out.println("today = " + today);
                if (givenDate.equals(today)){
                    publicOfferOneDay.add(getOfferFromRow(row));
                }
                else if(givenDate.equals(today.plusDays(3))){
                    publicOfferThreeDay.add(getOfferFromRow(row));
                }
                else if(givenDate.equals(today.plusDays(4))){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new PublicOfferLists(publicOfferOneDay, publicOfferThreeDay);
    }
    private PublicOffer getOfferFromRow(Element row){
        PublicOffer publicOffer = new PublicOffer();
        publicOffer.setItemName(Objects.requireNonNull(row.select("h4.item_name").first()).text().trim());
        publicOffer.setOfferingPrice(Objects.requireNonNull(row.select("li.area_price span.num").first()).text().trim());
        publicOffer.setLeadManager(Objects.requireNonNull(row.select("li.area_sup").first()).text().trim());
        publicOffer.setSector(Objects.requireNonNull(row.select("li.area_type").first()).text().trim());
        publicOffer.setScheduledDate(Objects.requireNonNull(row.select("li.area_private span.num").first()).text().trim());
        publicOffer.setListingDate(Objects.requireNonNull(row.select("li.area_list span.num").first()).text().trim());
        return publicOffer;
    }

    private void sendMsg(PublicOfferLists publicOfferLists) {

        for(PublicOffer publicOffer : publicOfferLists.getListThreeDay()){
            System.out.println("publicOffer = " + publicOffer);
        }
        for (PublicOffer publicOffer: publicOfferLists.getListOneDay()){
            System.out.println("publicOffer = " + publicOffer);
        }
    }
}
