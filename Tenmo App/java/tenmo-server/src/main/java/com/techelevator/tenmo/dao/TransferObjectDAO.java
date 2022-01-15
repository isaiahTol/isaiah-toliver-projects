package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferObject;

import java.util.List;

public interface TransferObjectDAO {

    public List<TransferObject> getTransferObjects(int accountID);

    public TransferObject getTransferObject(int transferID);

    public TransferObject transferToAccountTransaction(TransferObject TransferObjectToSave);

    public void removeTransferObject(int transferID);

    public void editTransferObject(TransferObject transferObjectToSave);

}
