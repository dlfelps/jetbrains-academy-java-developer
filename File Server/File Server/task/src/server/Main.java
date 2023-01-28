package server;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //System.out.println("Server started!");

        //HashMap<String, String> idMap = new HashMap<String, String>();
        HashMap<String, String> idMap = (HashMap<String, String>) loadIdMap();
        //loadIdMap(idMap)
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));

        while(true) {
            Socket socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
            String request = "init", response;
            String[] requestTokens;

            try {
                request = input.readUTF();
            }
            catch (SocketException e) {
                //
            }
            //System.out.println(request);

            requestTokens = request.split(" ", 3);

            if (requestTokens[0].equals("exit")) {
                output.writeUTF("Ending.");
                saveIdMap(idMap);
                input.close();
                output.close();
                socket.close();
                server.close();
                break;
            }

            switch(requestTokens[0]) {
                case "PUT":
                    response = addFile(requestTokens, input, idMap);
                    output.writeUTF(response);
                    break;
                case "GET":
                    response = getFile(requestTokens, output, idMap);
                    break;
                case "DELETE":
                    response = deleteFile(requestTokens, output, idMap);
                    break;
                default:
                    response = "nope";
                    break;
            }
            //System.out.println(response);
            try {
                output.writeUTF(response);
            }
            catch (SocketException e){
                //
            }

            request = "";
            input.close();
            output.close();
            socket.close();
        }
    }

    public static String addFile(String[] requestTokens, DataInputStream input, HashMap<String, String> idMap) throws IOException {
        String filename;
        String fileId = System.currentTimeMillis() + "";
        if (requestTokens[1].equals("*"))
            filename = fileId + ".dat";
        else
            filename = requestTokens[1];
        String path = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\" + filename;
        File f = new File(path);
        String response = "";

        //Read in file size, file data
        int size = input.readInt();
        byte[] fileBytes = new byte[size];
        input.readFully(fileBytes, 0, fileBytes.length);

        if(!f.exists() && !f.isDirectory()) {
            Files.write(Paths.get(path), fileBytes);
            response = "200 " + fileId;

            //Update id - name map
            idMap.put(fileId, filename);
        }
        else {
            response = "403";
        }
        return response;
    }

    public static String getFile(String[] requestTokens, DataOutputStream output, HashMap<String, String> idMap) throws IOException {
        String filename, response;
        if (requestTokens[1].equals("BY_ID")){
            if (idMap.containsKey(requestTokens[2]))
                filename = idMap.get(requestTokens[2]);
            else {
                response = "404";
                output.writeUTF(response);
                return response;
            }
        }
        else{
            filename = requestTokens[2];
        }
        String path = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\" + filename;
        File f = new File(path);

        if(f.exists() && !f.isDirectory()) {
            response = "200";
            output.writeUTF(response);
            byte[] fileBytes = Files.readAllBytes(Paths.get(path));
            output.writeInt(fileBytes.length);
            output.write(fileBytes);
            output.flush();
        }
        else {
            response = "404";
            output.writeUTF(response);
        }
        return response;
    }

    public static String deleteFile(String[] requestTokens, DataOutputStream output, HashMap<String, String> idMap) throws IOException {
        String filename, fileId;
        String response = "";
        if (requestTokens[1].equals("BY_ID")){
            if (idMap.containsKey(requestTokens[2])) {
                filename = idMap.get(requestTokens[2]);
                fileId = requestTokens[2];
            }
            else {
                response = "404";
                output.writeUTF(response);
                return response;
            }

        }
        else{
            filename = requestTokens[2];
            fileId = findId(idMap, requestTokens[2]);
        }
        String path = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\" + filename;
        File f = new File(path);


        if (f.exists() && !f.isDirectory()) {
            f.delete();
            idMap.remove(fileId);
            response = "200";
        }
        else {
            response = "404";
        }
        output.writeUTF(response);
        return response;
    }

    public static void saveIdMap(Object obj) throws IOException{
        String filename = "C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\map.bin";
        FileOutputStream fos = new FileOutputStream(filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    public static Object loadIdMap() throws IOException, ClassNotFoundException {
        //Check file exists, return empty map if not

        FileInputStream fis = new FileInputStream("C:\\Users\\Nai\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\map.bin");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static String findId(HashMap<String, String> idMap, String toFind){
        for (String id : idMap.keySet()){
            if (id.equals(toFind))
                return id;
        }
        return "";
    }

    public static void addToList(String[] files, String filename){
        int lastOpen = -1;
        Pattern pattern = Pattern.compile("^file[1-9]0?$");
        Matcher matcher = pattern.matcher(filename);
        boolean matchFound = matcher.find();

        if(!matchFound) {
            System.out.println("Cannot add the file " + filename);
            return;
        }

        for (int i = 0; i < files.length; i++){
            if (filename.equals(files[i])){
                System.out.println("Cannot add the file " + filename);
                return ;
            }

            if (files[i] == null)
                lastOpen = i;
        }

        if (lastOpen >= 0){
            files[lastOpen] = filename;
            System.out.println("The file " + filename + " added successfully");
        }
        else {
            System.out.println("Cannot add the file " + filename);
        }

        return;
    }

    public static void getFromList(String files[], String filename){

        for (int i = 0; i < files.length; i++){
            if (filename.equals(files[i])){
                System.out.println("The file " + filename + " was sent");
                return ;
            }
        }
        System.out.println("The file " + filename + " not found");
        return;
    }

    public static void deleteFromList(String files[], String filename){

        for (int i = 0; i < files.length; i++){
            if (filename.equals(files[i])){
                files[i] = null;
                System.out.println("The file " + filename + " was deleted");
                return ;
            }
        }
        System.out.println("The file " + filename + " not found");
        return;
    }
}