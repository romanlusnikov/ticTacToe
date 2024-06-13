package serv;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private int iClient;

    public ServerThread(Socket clientSocket, int iClient) {
        this.clientSocket = clientSocket;
        this.iClient = iClient;
    }

    public void run() {
        try {
            InputStream sin = clientSocket.getInputStream();
            OutputStream sout = clientSocket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            out.writeUTF(convert(Temp.FIELD));

            String motion;
            motion = in.readUTF();

            makeMove(motion, iClient);

            check(iClient);
            if (!Temp.WIN.equals("") || Temp.COUNT_MOTION==9) {

                return;
            }

            out.writeUTF(convert(Temp.FIELD));

        } catch (IOException e) {
            System.out.println("Ошибка: " + e);
        }
    }

    public String convert(String[] field) {
        String fieldStr = "";
        for (int i = 0; i < 9; i++) {
            fieldStr += field[i];
        }
        return fieldStr;
    }

    public static void makeMove(String motion, int iClient) {
        for (int i = 0; i < 9; i++) {
            if (Integer.valueOf(motion) - 1 == i) {
                if (iClient == 0) {
                    Temp.FIELD[i] = "X";
                }
                if (iClient == 1) {
                    Temp.FIELD[i] = "O";
                }
            }
        }
    }

    public void check(int iClient) {
        Temp.COUNT_MOTION++;
        System.out.println("Ход: " + Temp.COUNT_MOTION);
        System.out.println();
        if(Temp.COUNT_MOTION==9){
            return;
        }
        String move = "";
        if (iClient == 0) {
            move = "X";
        }
        if (iClient == 1) {
            move = "O";
        }

        //ряды
        if (Temp.FIELD[0].equals(move) & Temp.FIELD[1].equals(move) & Temp.FIELD[2].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в ряду 1";
        }
        if (Temp.FIELD[3].equals(move) & Temp.FIELD[4].equals(move) & Temp.FIELD[5].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в ряду 2";
        }
        if (Temp.FIELD[6].equals(move) & Temp.FIELD[7].equals(move) & Temp.FIELD[8].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в ряду 3";
        }
        //колонны
        if (Temp.FIELD[0].equals(move) & Temp.FIELD[3].equals(move) & Temp.FIELD[6].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в колонке 1";
        }
        if (Temp.FIELD[1].equals(move) & Temp.FIELD[4].equals(move) & Temp.FIELD[7].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в колонке 2";
        }
        if (Temp.FIELD[2].equals(move) & Temp.FIELD[5].equals(move) & Temp.FIELD[8].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в колонке 3";
        }
        //диагонали
        if (Temp.FIELD[0].equals(move) & Temp.FIELD[4].equals(move) & Temp.FIELD[8].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в диагонали";
        }
        if (Temp.FIELD[2].equals(move) & Temp.FIELD[4].equals(move) & Temp.FIELD[6].equals(move)) {
            Temp.WIN = "Выиграл " + move + " в диагонали";
        }
    }
}
