package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferObjectDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferObject;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDao userDao;

    @Autowired
    TransferObjectDAO transferObjectDAO;

    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public BigDecimal obtainBalance(Principal principal) {

        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);

        BigDecimal balance = accountDAO.retrieveBalanceWithUserID(userId);
        Accounts accounts = new Accounts();
        accounts.setBalance(balance);

        return accounts.getBalance();
    }


    @RequestMapping(path="/transferMoney",method = RequestMethod.POST)
    public void sendMoney(@RequestBody TransferObject transferObject){
        transferObjectDAO.transferToAccountTransaction(transferObject);
        int fromAccountID = accountDAO.retrieveAccountID(transferObject.getUserFromID());
        int toAccountID = accountDAO.retrieveAccountID(transferObject.getUserToID());
        BigDecimal transferredAmount = transferObject.getAmountToTransfer();

        accountDAO.addToBalance(transferredAmount, toAccountID);
        accountDAO.subtractFromBalance(transferredAmount, fromAccountID);
    }

    @RequestMapping(path="/transfers",method = RequestMethod.GET)
    public List<TransferObject> retrieveTransfersForUser(Principal principal) {
        String name = principal.getName();
        int userID = userDao.findIdByUsername(name);
        int accountID = accountDAO.retrieveAccountID(userID);
        return transferObjectDAO.getTransferObjects(accountID);

    }

    /*Built*/
    @RequestMapping(path="/account",method = RequestMethod.GET)
    public Accounts retrieveAccount(Principal principal) {

        String name = principal.getName();
        int userID = userDao.findIdByUsername(name);

        return accountDAO.retrieveAccount(userID);
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> retrieveUsers(Principal principal){

        String name = principal.getName();
        List<User> allUsers = userDao.findAll();
        return allUsers;
    }

}

