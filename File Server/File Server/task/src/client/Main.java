package client;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println("Client started!");
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        String command, filename = "", serverFilename = "", cmdString = "", nameOrId = "";

        //Might have to change 2..
        System.out.print("Enter action (1 - get the file, 2 - create a file, 3 - delete the file): ");
        command = scanner.nextLine();
        switch(command){
            case "1":
                command = "GET";
                System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id):");
                nameOrId = scanner.nextLine();
                if (nameOrId.equals("1")) {
                    nameOrId = "BY_NAME";
                    System.out.print("Enter filename: ");
                }
                else {
                    nameOrId = "BY_ID";
                    System.out.print("Enter id: ");
                }
                filename = scanner.nextLine();
                cmdString = command + " " + nameOrId + " " + filename;
                break;
            case "2":
                command = "PUT";
                System.out.print("Enter name of the file: ");
                filename = scanner.nextLine();
                System.out.print("Enter name of the file to be saved on server: ");
                serverFilename = scanner.nextLine();
                if (serverFilename.trim().length() == 0)
                    serverFilename = "*";
                cmdString = command + " " + serverFilename;
                break;
            case "3":
                command = "DELETE";
                System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
                nameOrId = scanner.nextLine();
                if (nameOrId.equals("1")) {
                    nameOrId = "BY_NAME";
                    System.out.print("Enter filename: ");
                }
                else {
                    nameOrId = "BY_ID";
                    System.out.print("Enter id: ");
                }
                filename = scanner.nextLine();
                cmdString = command + " " + nameOrId + " " + filename;
                break;
            default:
                cmdString = command;
                break;
        }


        output.writeUTF(cmdString);
        output.flush();
        if (command.equals("PUT")) {
            transmitFile(filename, output);
        }
        System.out.println("The request was sent.");
        String response = input.readUTF();
        String[] responseTokens = response.split(" ", 2);

        switch (responseTokens[0]){
            case "200":
                if (command.equals("GET")) {
                    receiveFile(input);
                }
                else if (command.equals("PUT")) {
                    System.out.println("Response says that file is saved! ID = " + responseTokens[1]);
                }
                else if (command.equals("DELETE")) {
                    System.out.println("The response says that this file was deleted successfully!");
                }
                break;
            case "404":
                System.out.println("The response says that this file is not found!");
                break;
            case "403":
                System.out.println("Ok, the response says that creating the file was forbidden!");
                break;
            default:
                //System.out.println("defaulted..." + response);
                break;
        }

    }

    public static void transmitFile(String filename, DataOutputStream output) throws IOException {
        String filePath = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\client\\data\\" + filename;
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        output.writeInt(fileBytes.length);
        output.write(fileBytes);
        output.flush();
    }

    public static void receiveFile(DataInputStream input) throws IOException {
        int size = input.readInt();
        byte[] fileBytes = new byte[size];
        input.readFully(fileBytes, 0, fileBytes.length);
        System.out.print("The file was downloaded! Specify a name for it: ");
        Scanner scanner = new Scanner(System.in);
        String saveName = scanner.nextLine();
        String filePath = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\client\\data\\" + saveName;
        Files.write(Paths.get(filePath), fileBytes);
        System.out.println("File saved on the hard drive!");
    }
}