package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferObject;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.math.BigDecimal;


public class TenmoService {

    private RestTemplate restTemplate = new RestTemplate();
    private  String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal retrieveBalance() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Accounts accounts = restTemplate.exchange("http://localhost:8080/account", HttpMethod.GET, entity, Accounts.class).getBody();
        return accounts.getBalance();
    }

    public User[] getAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        User[] allUsers = restTemplate.exchange("http://localhost:8080/users", HttpMethod.GET, entity, User[].class).getBody();
        return allUsers;
    }


}




