package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService smService;
    
    public SocialMediaController(){
        smService = new SocialMediaService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register",this::postUserHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountID);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postUserHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        Account addedUser = smService.addNewAccount(user);
        if(addedUser == null || addedUser.getUsername().length() == 0){
            ctx.status(400);
        } else{
            ctx.json(mapper.writeValueAsString(addedUser));
            ctx.status(200);
        }
        
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        Account loginnedAccount = smService.loginAccount(user);

        if(loginnedAccount == null){
            ctx.status(401);
        } else{
            ctx.json(mapper.writeValueAsString(loginnedAccount));
            ctx.status(200);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage = mapper.readValue(ctx.body(), Message.class);

        if(newMessage.getMessage_text().length() == 0 || newMessage.getMessage_text().length() > 255){
            ctx.status(400);
        }

        Message postedMessage = smService.newMessage(newMessage);
        if(postedMessage == null){
            ctx.status(400);
        } else{
            ctx.json(mapper.writeValueAsString(postedMessage));
            ctx.status(200);
        }

    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(smService.getAllMessages()));
        ctx.status(200);
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int msgID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = smService.getMessageByID(msgID);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
            ctx.status(200);
        } else{
            ctx.status(200);
            ctx.body();
        }
        
    }

    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int msgID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = smService.deleteMessageByID(msgID);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
            ctx.status(200);
        } else{
            ctx.status(200);
            ctx.body();
        }
        
    }

    private void patchMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int msgID = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message updMsg = smService.updateMessageByID(msgID, msg.getMessage_text());

        if(updMsg == null){
            ctx.status(400);
        } else{
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(updMsg));
        }
    }

    private void getAllMessagesByAccountID(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(mapper.writeValueAsString(smService.getAllMessagesByAccountID(accountID)));
        ctx.status(200);
    }

}