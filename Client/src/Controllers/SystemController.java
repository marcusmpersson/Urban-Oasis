package Controllers;
public class SystemController {
    private ClientConnection clientConnection;
    private LoginHandler loginHandler;
    public SystemController(){
        this.clientConnection = new ClientConnection();
        this.loginHandler = new LoginHandler();
    }
    public void login(String userName, String password){
        clientConnection.setJwtToken(loginHandler.login(userName,password));
    }
}
