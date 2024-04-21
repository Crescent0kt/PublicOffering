package com.example.publicoffering.service;

import com.example.publicoffering.model.PublicOffer;
import com.example.publicoffering.model.PublicOfferLists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PublicOfferService {
    public PublicOfferLists Crawling() {
        List<PublicOffer> publicOfferOneDay = new ArrayList<>();
        List<PublicOffer> publicOfferThreeDay = new ArrayList<>();

        try {
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
                LocalDate givenDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yy.MM.dd"));
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


}
