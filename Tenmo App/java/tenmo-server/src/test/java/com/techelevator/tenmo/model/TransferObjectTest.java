package com.techelevator.tenmo.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferObjectTest {

    @Test
    public void test_get_tansfer_ID() {
        TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));
        int expectedTransferID = 3008;
        int actualTransferID = transferObject.getTransferID();
        int falseTransferID = 2000;

        Assert.assertEquals(expectedTransferID,actualTransferID);
        assertNotEquals(falseTransferID,actualTransferID);

    }

    @Test
    public void test_get_transfer_type_ID() {
        TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));

        int expectedTransferTypeID = 1;
        int actualTransferTypeID = transferObject.getTransferTypeID();
        int falseTransferTypeID = 3;

        Assert.assertEquals(expectedTransferTypeID,actualTransferTypeID);
        assertNotEquals(falseTransferTypeID,actualTransferTypeID);
    }

    @Test
    public void test_getTransferStatusID() {

        TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));
        int expectedTransferStatusID = 2;
        int actualTransferStatusID = transferObject.getTransferStatusID();
        int falseTransferStatusID = 4;

        Assert.assertEquals(expectedTransferStatusID,actualTransferStatusID);
        assertNotEquals(falseTransferStatusID,actualTransferStatusID);

    }

//    @Test
//    public void getAccountFromName() {
//      TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));
//
//    }
//
//    @Test
//    public void getAccountToName() {
//      TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));
//
//
//
//    }
//

    @Test
    public void test_getAmountToTransfer() {

        TransferObject transferObject = new TransferObject(3008, 1,2,2024,2025, "moneyperson", "money", new BigDecimal("10.00"));

        BigDecimal expectedAmountToTransfer =new BigDecimal("10.00");
        BigDecimal actualAmountToTransfer = transferObject.getAmountToTransfer();
        BigDecimal falseAmountToTransfer = new BigDecimal("-66.00");

        Assert.assertEquals(expectedAmountToTransfer,actualAmountToTransfer);
        assertNotEquals(falseAmountToTransfer,actualAmountToTransfer);

    }


}