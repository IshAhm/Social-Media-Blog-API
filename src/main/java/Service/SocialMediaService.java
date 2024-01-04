package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;


public class SocialMediaService {
    SocialMediaDAO smDAO;

    public SocialMediaService(){
        smDAO = new SocialMediaDAO();
    }

    public SocialMediaService(SocialMediaDAO smDAO){
        this.smDAO = smDAO;
    }

    public Account addNewAccount(Account user){
        if(user.getUsername() == null){
            return null;
        }
        if(user.getPassword().length() < 4){
            return null;
        }
        if(smDAO.userExistsByUsername(user.getUsername())){
            return null;
        }

        return smDAO.addNewAccount(user);
    }

    public Account loginAccount(Account user){
        return smDAO.login(user);
    }

    public Message newMessage(Message message){
        
        if(message.getMessage_text().length() == 0){
            return null;
        }

        if(smDAO.userExistsByID(message.getPosted_by())){
            return smDAO.postNewMessage(message);
        } else{
            return null;
        }
    }

    public List<Message> getAllMessages(){
        return smDAO.getAllMessages();
    }

    public Message getMessageByID(int msgID){
        return smDAO.getMessageByID(msgID);
    }

    public Message deleteMessageByID(int msgID){
        return smDAO.deleteMessageByID(msgID);
    }
    
    public Message updateMessageByID(int msgID, String newMsgBody){
        if(newMsgBody.length()>255 || newMsgBody.equals("")){
            return null;
        }
        return smDAO.updateMessageByID(msgID, newMsgBody);
    }

    public List<Message> getAllMessagesByAccountID(int accountID){
        return smDAO.getAllMessagesByAccountID(accountID);
    }
}
