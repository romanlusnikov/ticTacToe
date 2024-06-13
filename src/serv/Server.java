package serv;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static ServerSocket serverSocket;
    private static Socket[] clientSockets;

    public void start(int port) throws IOException, InterruptedException {
        serverSocket = new ServerSocket(port);
        clientSockets = new Socket[2];

        for (int i = 0; i < clientSockets.length; i++) {
            clientSockets[i] = serverSocket.accept();
            System.out.println("Игрок подключен!");
        }
        System.out.println("Игроки подключены! \nИгра начинается!!!!");
        while (true) {
            if (Temp.COUNT_MOTION==9) {
                writeServ(Temp.DRAW);
                return;
            }else if(Temp.WIN.equals("")) {
                game();
            }else{
                writeServ(Temp.WIN);
                return;
            }
        }

    }

    private void writeServ(String string) {
        try {
            OutputStream soutOnePlayer = clientSockets[0].getOutputStream();
            DataOutputStream outOnePlayer = new DataOutputStream(soutOnePlayer);
            OutputStream soutTwoPlayer = clientSockets[1].getOutputStream();
            DataOutputStream outTwoPlayer = new DataOutputStream(soutTwoPlayer);

            outOnePlayer.writeUTF(string);
            outTwoPlayer.writeUTF(string);

            Thread.sleep(3000);

            soutOnePlayer.close();
            soutTwoPlayer.close();
            stop();
        } catch (Exception e) {
        }
    }

    private void game() throws InterruptedException {
        for (int i = 0; i < clientSockets.length; i++) {
            if (!Temp.WIN.equals("") | Temp.COUNT_MOTION==9) {
                break;
            } else {
                Thread thread = new Thread(new ServerThread(clientSockets[i], i));
                thread.start();
                thread.join();
            }
        }
    }

    public static void stop() {
        try {
            clientSockets[0].close();
            clientSockets[1].close();
            serverSocket.close();
            System.out.println("Close...");
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        server.start(4004);
    }
}
