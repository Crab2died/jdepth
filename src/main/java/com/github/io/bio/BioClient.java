package com.github.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BioClient {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8200;

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(socket.getInputStream())
             );
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
        ) {
            for (; ; ) {
                System.out.print("Please input something:");
                Scanner scan = new Scanner(System.in);
                String str = scan.nextLine();
                System.out.println(str);
                pw.println(str);
                String resp = br.readLine();
                if (null != resp) {
                    System.out.println("Response is :" + resp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
