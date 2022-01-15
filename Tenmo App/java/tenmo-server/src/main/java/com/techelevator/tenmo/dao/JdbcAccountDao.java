package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal retrieveBalanceWithUserID(int userId) {

        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        BigDecimal balance = null;

        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }

        return balance;
    }

    @Override
    public BigDecimal retrieveBalanceWithAccountID(int accountID){
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);

        BigDecimal balance = null;

        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    /*Built*/
    @Override
    public Accounts retrieveAccount(int userID) {
        String sql = "SELECT * FROM accounts WHERE user_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);

        Accounts account = null;
        if(results.next()) {
            int accountId = results.getInt("account_id");
            int userId = results.getInt("user_id");
            BigDecimal balance = results.getBigDecimal("balance");

            account = new Accounts(accountId, userId, balance);
        }
        return account;
    }

    @Override
    public int retrieveAccountID(int userID) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);

        int local = 2;
        if(results.next()) {
             local = results.getInt("account_id");
            long id = Long.valueOf(local);
        }

        return local;
    }

    @Override
    public void addToBalance(BigDecimal transferredAmount, int toAccountID) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        BigDecimal updatedBalance = retrieveBalanceWithAccountID(toAccountID).add(transferredAmount);
        jdbcTemplate.update(sql,updatedBalance,toAccountID);
    }

    @Override
    public void subtractFromBalance(BigDecimal transferredAmount,int fromUserID) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        BigDecimal updatedBalance = retrieveBalanceWithAccountID(fromUserID).subtract(transferredAmount);
        jdbcTemplate.update(sql,updatedBalance,fromUserID);
    }
}
