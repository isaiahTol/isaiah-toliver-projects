package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferObject {

    private int transferID;
    private int transferTypeID;
    private int transferStatusID;
    private int userFromID;
    private int userToID;
    private String accountFromName;
    private String accountToName;
    private BigDecimal amountToTransfer;


    public TransferObject(int transferID, int transferTypeID, int transferStatusID, int accountFromID, int accountToID, String accountFromName, String accountToName, BigDecimal amountToTransfer) {
        this.transferID = transferID;
        this.transferTypeID = transferTypeID;
        this.transferStatusID = transferStatusID;
        this.userFromID = accountFromID;
        this.userToID = accountToID;
        this.accountFromName = accountFromName;
        this.accountToName = accountToName;
        this.amountToTransfer = amountToTransfer;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public int getTransferTypeID() {
        return transferTypeID;
    }

    public void setTransferTypeID(int transferTypeID) {
        this.transferTypeID = transferTypeID;
    }

    public int getTransferStatusID() {
        return transferStatusID;
    }

    public void setTransferStatusID(int transferStatusID) {
        this.transferStatusID = transferStatusID;
    }

    public int getUserFromID() {
        return userFromID;
    }

    public void setUserFromID(int userFromID) {
        this.userFromID = userFromID;
    }

    public int getUserToID() {
        return userToID;
    }

    public void setUserToID(int userToID) {
        this.userToID = userToID;
    }

    public BigDecimal getAmountToTransfer() {
        return amountToTransfer;
    }

    public String getAccountFromName() {
        return accountFromName;
    }

    public void setAccountFromName(String accountFromName) {
        this.accountFromName = accountFromName;
    }

    public String getAccountToName() {
        return accountToName;
    }

    public void setAccountToName(String accountToName) {
        this.accountToName = accountToName;
    }

    public void setAmountToTransfer(BigDecimal amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }
}


