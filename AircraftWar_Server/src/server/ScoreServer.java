package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ScoreServer {
        private String content = "";

        private String name;
        private int currentScore;
        private int difficulty;

        RankOnline rankOnline;

        public ScoreServer(){
            try{
                InetAddress addr = InetAddress.getLocalHost();
                System.out.println("local host:" + addr);
                //创建server socket
                ServerSocket rankServerSocket = new ServerSocket(9996);
                System.out.println("listen port 9996");

                while(true){
                    System.out.println("----------waiting for rankClient connect----------");
                    Socket scoreSocket1 = rankServerSocket.accept();
                    System.out.println("accept rankClient connect" + scoreSocket1);
                    new Thread(new ScoreServer.Service(scoreSocket1)).start();
//                    Socket scoreSocket2 = rankServerSocket.accept();
//                    System.out.println("accept client connect" + scoreSocket2);
//                    new Thread(new ScoreServer.Service(scoreSocket2)).start();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        class Service implements Runnable{
            private Socket socket;
            private BufferedReader in = null;


            public Service(Socket socket){
                this.socket = socket;
                try{
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }


            @Override
            public void run() {
                System.out.println("waiting for rankClient message... " );
                try {
                    while ((content = in.readLine()) != null) {
                        System.out.println("message from scoreClient:"+content);
                        if(content.equals("bye")){
                            System.out.println("disconnect from client,close socket");
                            socket.shutdownInput();
                            socket.shutdownOutput();
                            socket.close();
                        }else {
                            String[] nameScoreData = content.split(",");
                            name = nameScoreData[0];
                            currentScore = Integer.parseInt(nameScoreData[1]);
                            difficulty = Integer.parseInt(nameScoreData[2]);
                            rankOnline = new RankOnline(difficulty,name,currentScore);
//                            System.out.println(rankOnline.rankBench());
                            this.sendMessge(socket);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            public void sendMessge(Socket socket) {
                PrintWriter pout = null;
                try{
                    String message = "";
                    message = rankOnline.rankBench();
                    System.out.println("message to scoreClient:" + message);
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),true);
                    pout.println(message);
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
