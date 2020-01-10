package com.unict.dieei.pr20.videomanagementservice.controller;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class PingController {

    @GetMapping(path = "/ping")
    public @ResponseBody ResponseEntity<String> ping(@RequestHeader("X-REQUEST-ID") String[] requestIds) {
        // Send GET request to video processing service
        RestTemplate restTemplate = new RestTemplate();
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(3000);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-REQUEST-ID", requestIds[1]);
        URI url = URI.create("http://video-processing:5000/ping");
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, url);
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody = null;
        long sendTime = System.currentTimeMillis();
        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            responseBody = "pong [Video Management Service]<br>" + response.getBody();
        } catch(ResourceAccessException e) {
            responseBody = "pong [Video Management Service]<br>Video Processing Service not running";
        }
        long communicationDelay = System.currentTimeMillis() - sendTime;
        responseHeaders.set("X-Communication-Delay", String.valueOf(communicationDelay));
        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }
}
