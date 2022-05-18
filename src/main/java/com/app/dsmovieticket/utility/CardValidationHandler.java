package com.app.dsmovieticket.utility;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class CardValidationHandler {

    private final RestTemplate restTemplate;
    private final Environment environment;

    public void deductAmount(String cardType, String cardNumber, String csv, String cardHolder, float amount) {

        String postURL = environment.getProperty("dsmovieticketpayment.deduct-amount");

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(environment.getProperty("dsmovieticketpayment.key"));

        Map<String, Object> map = new HashMap<>();
        map.put("cardType", cardType);
        map.put("cardNumber", cardNumber);
        map.put("csv", csv);
        map.put("cardHolder", cardHolder);
        map.put("amount", amount);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = this.restTemplate.postForEntity(postURL, httpEntity, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Card Validation Failed : " + e.getMessage());
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            log.info("Card Validation Success");
        } else {
            throw new RuntimeException("Card Validation Failed");
        }
    }

}