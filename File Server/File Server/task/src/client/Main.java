package client;

import java.io.*;
import java.net.*;


public class Main {

    private static final int PORT = 23456;
    private static final String ADDRESS = "127.0.0.1";

    public static void main(String[] args) {

        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String outgoing = "Give me everything you have!";
            output.writeUTF(outgoing);
            System.out.printf("Sent: %s%n", outgoing);
            String msg = input.readUTF();
            System.out.printf("Received: %s%n", msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
