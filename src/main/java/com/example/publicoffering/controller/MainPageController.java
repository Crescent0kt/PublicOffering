package com.example.publicoffering.controller;

import com.example.publicoffering.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    private final MainService mainService;

    public MainPageController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/healthcheck")
    public String healthCheck(){
        return "Health";
    }
}
