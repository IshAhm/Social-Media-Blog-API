package DAO;

import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;


public class SocialMediaDAO {
    
    public Account addNewAccount(Account user){
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "INSERT INTO account (username, password) VALUES (?,?)" ;
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
            
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
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

    public Boolean userExistsByID(int userID){
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userID);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            } else{
                return false;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Boolean userExistsByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            } else{
                return false;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }
    
    
    public Message postNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
        
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int msgID){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, msgID);
        
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }else {
                return null;
            }
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Message deleteMessageByID(int msgID){
        Connection connection = ConnectionUtil.getConnection();

        try{
            
            Message deletedMsg = getMessageByID(msgID);

            String sqlDelete = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatementDelete = connection.prepareStatement(sqlDelete);
            preparedStatementDelete.setInt(1, msgID);
            preparedStatementDelete.executeUpdate();
            return deletedMsg;
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Boolean checkIfMessageExistsByID(int msgID){

        Message existingMsg = getMessageByID(msgID);

        if(existingMsg == null){
            return false;
        } else{
            return true;
        }

    }

    public Message updateMessageByID(int msgID, String newMsgBody){
        Connection connection = ConnectionUtil.getConnection();

        try{
            
            if(!checkIfMessageExistsByID(msgID)){
                return null;
            }

            String sql = "UPDATE message SET message_text = ? WHERE message_id =?";
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(sql);
            preparedStatementUpdate.setString(1, newMsgBody);
            preparedStatementUpdate.setInt(2, msgID);
            preparedStatementUpdate.executeUpdate();
            
            return getMessageByID(msgID);
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesByAccountID(int accountID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

}
