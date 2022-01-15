package com.techelevator.tenmo.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountsTest {

    @Test
    public void testGetAccountID(){
        Accounts account = new Accounts(1,2, new BigDecimal(15.00));

        int expectedAccountID = 1;
        int actualAccountID = account.getAccountID();
        int falseAccountID = 2;

        Assert.assertEquals(expectedAccountID,actualAccountID);
        assertNotEquals(falseAccountID,actualAccountID);
    }

    @Test
    public void testGetUserID(){
        Accounts account = new Accounts(1,2,new BigDecimal(15.00));

        assertEquals(2,account.getUserID());
        assertNotEquals(1,account.getUserID());
    }

    @Test
    public void testGetBalance(){
        Accounts account = new Accounts(1,2,new BigDecimal(15.00));

        assertEquals(new BigDecimal(15.00), account.getBalance());
        assertNotEquals(new BigDecimal(20.00), account.getBalance());
    }

}