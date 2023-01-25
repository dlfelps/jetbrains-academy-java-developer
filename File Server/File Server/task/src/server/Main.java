package server;

import java.io.*;
import java.net.*;


class Session extends Thread {
    private final Socket socket;

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    @Override
    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String msg = input.readUTF();
            System.out.printf("Received: %s%n", msg);
            String outgoing = "All files were sent!";
            output.writeUTF(outgoing);
            System.out.printf("Sent: %s%n", outgoing);
            socket.close();//where the socket gets closed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    private static final int PORT = 23456;
    private static final String ADDRESS = "127.0.0.1";

    public static void main(String[] args) {

        System.out.println("Server started!");
        try (ServerSocket incoming = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) { //where the socket gets created

            Session session = new Session(incoming.accept());
            session.start(); // it does not block this thread

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}