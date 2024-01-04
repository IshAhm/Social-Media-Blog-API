package Service;

import DAO.SocialMediaDAO;
import Model.Account;

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

        
        return smDAO.addNewAccount(user);
    }

    public Account loginAccount(Account user){
        return smDAO.login(user);
    }
}
