package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferObjectJdbcDAO implements TransferObjectDAO{

    private JdbcTemplate template;

    @Autowired
    private AccountDAO accountDAO;


    public TransferObjectJdbcDAO(DataSource dataSource){
        template = new JdbcTemplate(dataSource);
    }


    @Override
    public List<TransferObject> getTransferObjects(int accountID) {

        String sqlGetAllTransferObjects = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, " +
                "t.account_from, t.account_to, u_from.username AS from_user, u_to.username AS to_user, " +
                "t.amount FROM transfers t " +
                "   JOIN accounts a_to on t.account_to = a_to.account_id " +
                "   JOIN accounts a_from ON t.account_from = a_from.account_id " +
                "   JOIN users u_to ON a_to.user_id = u_to.user_id " +
                "   JOIN users u_from ON a_from.user_id = u_from.user_id " +
                "   WHERE t.account_to = ? OR t.account_from = ?";
        List<TransferObject> transfersList = new ArrayList<TransferObject>();
        SqlRowSet result = template.queryForRowSet(sqlGetAllTransferObjects, accountID, accountID);

        while(result.next()) {
            int transferID = result.getInt("transfer_id");
            int transferTypeID = result.getInt("transfer_type_id");
            int transferStatusID = result.getInt("transfer_status_id");
            int accountFromID = result.getInt("account_from");
            int accountToID = result.getInt("account_to");
            String userFromName = result.getString("from_user");
            String userToName = result.getString("to_user");
            BigDecimal amountToTransfer = result.getBigDecimal("amount");

            TransferObject transferObject = new TransferObject(transferID, transferTypeID, transferStatusID, accountFromID, accountToID, userFromName,userToName, amountToTransfer);
            transfersList.add(transferObject);
        }
        return transfersList;
    }

    @Override
    public TransferObject getTransferObject(int transferID) {
        return null;
    }

    @Override
    public TransferObject transferToAccountTransaction(TransferObject transferObjectToSave) {
        int transferTypeID = transferObjectToSave.getTransferTypeID();
        int transferStatusID = transferObjectToSave.getTransferStatusID();

        BigDecimal amountToTransfer = transferObjectToSave.getAmountToTransfer();

        int accountFromID = accountDAO.retrieveAccountID(transferObjectToSave.getUserFromID());
        int accountToID = accountDAO.retrieveAccountID(transferObjectToSave.getUserToID());

        try {
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(?,?,?,?,?) RETURNING transfer_id";
            int id = template.queryForObject(sql, Integer.class, transferTypeID, transferStatusID, accountFromID, accountToID, amountToTransfer);
            return getTransferObject(id);
        }catch (Exception e) {
            System.out.println("Enter a valid ID");
            return null;
        }
    }

    @Override
    public void removeTransferObject(int transferID) {

    }

    @Override
    public void editTransferObject(TransferObject transferObjectToSave) {

    }

}
