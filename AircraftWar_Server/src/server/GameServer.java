package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class GameServer {
    private List<Socket> mList = new ArrayList<>();
    private static final int PORT = 9999;

//    ServerSocket server = null;

    private Map<String, String> nameScore = new HashMap<>();

    public GameServer() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);
            //创建server socket
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("listening port " + PORT);

            while (true) {
                System.out.println("----------waiting for gameClient connect----------");
                Socket gameClient = server.accept();
                System.out.println("Get client connected: " + gameClient);
                mList.add(gameClient);

                nameScore.clear();
                //执行线程
                new Thread(new GameServer.Service(gameClient)).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String content = "";

        public Service(Socket clientsocket) {
            this.socket = clientsocket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                content = "rival " + this.socket.getInetAddress() + "join the game";
                this.sendMessge(socket);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        @Override
        public void run() {
            System.out.println("waiting for gameClient message... ");
            try {
                while ((content = in.readLine()) != null) {
                    for (Socket s : mList) {
                        System.out.println("message from gameClient:" + content);
                        if (content.equals("bye")) {
                            System.out.println("disconnect from client,close socket");
                            in.close();
                            mList.remove(s);
                            socket.shutdownInput();
                            socket.shutdownOutput();
                            socket.close();
                        } else {
                            String[] nameScoreData = content.split(",");
                            nameScore.put(nameScoreData[0], nameScoreData[1]);
                            System.out.println(nameScore);
                            this.sendMessge(socket);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void sendMsg() {
            System.out.println(content);
            for (Socket mSocket : mList) {
                PrintWriter pout = null;
                try {
                    String message = "";
                    for (Map.Entry<String, String> entry : nameScore.entrySet()) {
                        message = "玩家:" + entry.getKey() + " 得分:" + entry.getValue() + " ";
                        System.out.println("message to client:" + message);
                        pout = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(mSocket.getOutputStream())), true);
                        pout.println(message);
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void sendMessge(Socket socket) {
            System.out.println(content);
            for (Socket mSocket : mList) {
                PrintWriter pout = null;
                try {
                    String message = "";
                    for (Map.Entry<String, String> entry : nameScore.entrySet()) {
                        message = message + entry.getKey() + "," + entry.getValue() + "\n";

                    }
                    System.out.println("message to client:" + message);
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mSocket.getOutputStream())), true);
                    pout.println(message);
//                    for (Map.Entry<String, String> entry : nameScore.entrySet()) {
//                        message = entry.getKey() + "," + entry.getValue();
//                        System.out.println("message to gameClient:" + message);
//                        pout = new PrintWriter(new BufferedWriter(
//                                new OutputStreamWriter(mSocket.getOutputStream())), true);
//                        pout.println(message);
//                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
