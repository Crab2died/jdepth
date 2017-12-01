package com.github.net.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BioServerHandler implements Runnable {

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter pw = new PrintWriter(this.socket.getOutputStream(), true)) {

            for (; ; ) {
                String line = br.readLine();
                if (line != null) {
                    System.out.println("request is :" + line);
                    pw.println(System.currentTimeMillis() + ":" + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            socket = null;
            Thread.interrupted();
        }
    }

}
