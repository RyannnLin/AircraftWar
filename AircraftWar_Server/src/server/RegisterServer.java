package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RegisterServer {
    private String content = "";
    private String name;
    private String password;


    public RegisterServer() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);
            //创建server socket
            ServerSocket registerServerSocket = new ServerSocket(9998);
            System.out.println("listen port 9998");

            while (true) {
                System.out.println("----------waiting for registerClient connect----------");
                Socket registerSocket1 = registerServerSocket.accept();
                System.out.println("accept client connect" + registerSocket1);
                new Thread(new RegisterServer.Service(registerSocket1)).start();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;


        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        @Override
        public void run() {
            System.out.println("waiting for register client message ");
            try {
                content = in.readLine();
                System.out.println(content);
                if (content != null) {
                    System.out.println("message from register client:" + content);
                    if (content.equals("bye")) {
                        System.out.println("disconnect from client,close socket");
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } else {
                        String[] nameScoreData = content.split(" ");
                        name = nameScoreData[0];
                        password = nameScoreData[1];
                        System.out.println(name);
                        System.out.println(password);
                        write();
//                            this.sendMessge(socket);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void write() throws IOException {
            try {
                FileWriter pw = new FileWriter("src/users.txt", true);
                pw.write(name + ',');
                pw.write(password + '\n');
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//            public void sendMessge(Socket socket) {
//                PrintWriter pout = null;
//                try{
//                    String message = "";
//                    message = rankOnline.rankBench();
//                    System.out.println("messge to client:" + message);
//                    pout = new PrintWriter(new BufferedWriter(
//                            new OutputStreamWriter(socket.getOutputStream())),true);
//                    pout.println(message);
//                }catch (IOException ex){
//                    ex.printStackTrace();
//                }
//            }

    }
}
