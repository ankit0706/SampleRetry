package com.sample.retrypattern.controller;

import com.sample.retrypattern.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController {

    @Autowired
    CustomService customService;

    @GetMapping("/currentDateTime")
    public String getCurrentDateTime() {
        return java.time.LocalDateTime.now().toString();
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/icctest")
    public String getTestChampionShipInfo(@RequestParam(value = "countryName", defaultValue = "Unknown") String countryName) {
        return customService.downloadAndSendData(countryName);
    }
}
