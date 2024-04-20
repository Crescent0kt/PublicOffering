package com.example.publicoffering.model;
import lombok.Data;
import java.util.List;
@Data
public class PublicOfferLists{
    private List<PublicOffer> listOneDay;
    private List<PublicOffer> listThreeDay;

    public PublicOfferLists(List<PublicOffer> publicOfferOneDay, List<PublicOffer> publicOfferThreeDay) {
        listOneDay = publicOfferOneDay;
        listThreeDay = publicOfferThreeDay;
    }
}