package com.example.publicoffering.model;

import lombok.Data;

@Data
public class PublicOffer {
    String itemName;
    String offeringPrice;
    String leadManager;
    String sector;
    String scheduledDate;
    String listingDate;
}
