import server.LoginServer;
import server.ScoreServer;
import server.RegisterServer;
import server.GameServer;

public class MyServer {
    public static void main(String args[]){
        new MyServer();
    }

    public  MyServer(){
        new Thread(GameServer::new).start();
        new Thread(ScoreServer::new).start();
        new Thread(RegisterServer::new).start();
        new Thread(LoginServer::new).start();
    }


}