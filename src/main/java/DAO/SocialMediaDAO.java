package DAO;

import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;


public class SocialMediaDAO {
    
    public Account addNewAccount(Account user){
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "INSERT INTO Account (username, password) VALUES (?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_user_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_user_id, user.getUsername(), user.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account user){
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            String sql = "SELECT * FROM flight WHERE username = '?' AND password = '?';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account retrievedAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return retrievedAccount;
            } else{
                return null;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message postNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        
    }

}
