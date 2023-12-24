package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoginServer {
    private String content = "";
    private String name;
    private String password;
    public static final int port =9997;


    public LoginServer() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);
            //创建server socket
            ServerSocket loginServerSocket = new ServerSocket(port);
            System.out.println("listen port 9997");

            while (true) {
                System.out.println("----------waiting for loginClient connect...----------");
                Socket loginSocket1 = loginServerSocket.accept();
                new Thread(new LoginServer.Service(loginSocket1)).start();
                System.out.println("accept login: " + loginSocket1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public boolean isUesrExsits() throws IOException {
        try {
            String path = "src/users.txt";
            //查看文件是否存在，若不存在，则新建
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<String> lines = Files.readAllLines(Paths.get(path));
            if (lines.size() == 0) {
                return false;
            }
            for (String item : lines) {
                String[] temp = item.split(",");
                String tempUserName = temp[0];
                String tempPasswd = temp[1];
                if (tempUserName.equals(name) && tempPasswd.equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
            System.out.println("waiting for loginClient message... ");
            try {
                while ((content = in.readLine()) != null) {
                    System.out.println("message from loginClient:" + content);//打印得到的信息
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
                        String message2Client = isUesrExsits()? "success" : "fail";
                        this.sendMessge(socket,message2Client);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void sendMessge(Socket socket, String message) {
            PrintWriter pout = null;
            try {
                System.out.println("message to client:" + message);
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                pout.println(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
