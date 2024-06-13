package clients;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client1 {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("192.168.1.4", 4004);
            InputStream sin = clientSocket.getInputStream();
            OutputStream sout = clientSocket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            while (true) {

                String field;

                field = in.readUTF();

                System.out.println();

                if (field.indexOf("В") == 0 | field.indexOf("Н") == 0) {
                    System.out.println(field);
                    Thread.sleep(3000);
                    clientSocket.close();
                    in.close();
                    out.close();
                    break;
                } else {
                    print(field);

                    String motion = checkMotion(field);
                    out.writeUTF(motion);

                    field = in.readUTF();
                    if (field.indexOf("В") == 0 | field.indexOf("Н") == 0) {
                        System.out.println(field);
                        Thread.sleep(3000);
                        clientSocket.close();
                        in.close();
                        out.close();
                        break;
                    }
                    print(field);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String checkMotion(String field) {
        Scanner sc = new Scanner(System.in);
        String motion;
        String [] fieldMassive=field.split("");

        System.out.println();

        System.out.println("Сделайте ход (от 1 - 9)");

        while(true) {
            motion = sc.next();
            if (reg(motion)) {
                if(!(fieldMassive[Integer.parseInt(motion)-1].equals("X")) & !(fieldMassive[Integer.parseInt(motion)-1].equals("O"))) {
                    break;
                }else{
                    System.out.println("Введены неверные данные, попробуйте еще раз!");
                }
            } else {
                System.out.println("Введены неверные данные, попробуйте еще раз!");
            }
        }
        return motion;
    }

    static boolean reg(String S) {

        Pattern p =Pattern.compile("[1-9]");
        Matcher m = p.matcher(S);

        return m.matches();
    }

    public static void print(String field) {
        String[] fieldStr = field.split("");
        int con = 1;
        for (int i = 0; i < 9; i++) {
            System.out.print(" | " + fieldStr[i] + " | ");
            if (con % 3 == 0 & con != 9) {
                System.out.print("\n -------------------- \n");
            }
            con++;
        }
        System.out.println();
    }
}