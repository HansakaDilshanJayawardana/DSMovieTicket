package com.app.dsmovieticket.email;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
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
public class EmailController {

    private final RestTemplate restTemplate;
    private final Environment environment;

    public void sendMail(String email, String subject, String body) {
        String postURL = environment.getProperty("mail.send-mail");

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("subject", subject);
        map.put("body", body);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = this.restTemplate.postForEntity(postURL, httpEntity, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Email Service Unavailable : " + e.getMessage());
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            log.info("Email Sent");
        } else {
            throw new RuntimeException("Email Service Unavailable");
        }
    }

}