package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class TransferObjectService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken){this.authToken = authToken;}

    public TransferObject [] userTransfers(){
        List<TransferObject> transferObjectList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        TransferObject[] transfer = restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.GET, entity, TransferObject[].class).getBody();

        return transfer;
    }

    public TransferObject makeTransaction(TransferObject transferObject){

        TransferObject returnedTransferObject =restTemplate.exchange(
                    "http://localhost:8080/transferMoney",
                    HttpMethod.POST,makeTransferObjectEntity(transferObject),
                    TransferObject.class
                    ).getBody();

        return returnedTransferObject;
    }

    private HttpEntity<TransferObject> makeTransferObjectEntity(TransferObject transferObject){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.authToken);

        HttpEntity<TransferObject> entity = new HttpEntity<>(transferObject,headers);
        return entity;
    }

    public TransferObject viewTransactionReport(int transferID ){
        TransferObject[] transfersToRead = userTransfers();
        TransferObject detailedTransfer = new TransferObject();
        for(TransferObject transferObject : transfersToRead){
            if(transferObject.getTransferID() == transferID) {
                detailedTransfer = transferObject;
            }
        }
        return detailedTransfer;
    }


}
