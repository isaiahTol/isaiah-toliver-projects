package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.math.BigDecimal;

public interface AccountDAO {


    public BigDecimal retrieveBalanceWithUserID(int userID);

    public BigDecimal retrieveBalanceWithAccountID(int accountID);

    public Accounts retrieveAccount(int accountID);  /* added */

    public int retrieveAccountID(int userID);

    public void addToBalance(BigDecimal transferredAmount, int toUserID);

    public void subtractFromBalance(BigDecimal transferredAmount,int fromUserID);




}
