package com.sample.retrypattern.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class CustomService {

    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay=1000))
    public String downloadAndSendData(String countryName) throws RuntimeException {
        if(countryName.equalsIgnoreCase("England")) {
            // since England has never won the ICC Test Championship, we simulate a failure
            throw new RuntimeException("Failed to download data for England");
        } else if(countryName.equalsIgnoreCase("Australia")) {
            return "Won ICC Test Championship 2023";
        } else if(countryName.equalsIgnoreCase("New Zealand")) {
            return "Won ICC Test Championship 2021";
        } else{
            return "Country yet to win ICC Test Championship";
        }

    }

    @Recover
    public String getMessageAfterMaxAttempts(RuntimeException e) {
        return "Max attempts reached. Please try again later.";
    }
}
