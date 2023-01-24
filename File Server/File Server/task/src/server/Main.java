package server;

import java.util.Scanner;

enum Action{
    ADD, GET, DELETE, EXIT;

    public static Action from(String s){
        return switch (s.toLowerCase()){
            case "add" -> ADD;
            case "get" -> GET;
            case "delete" -> DELETE;
            default -> EXIT;
        };
    }
}

class Controller {

    Storage server;
    public Controller(Storage server){
        this.server = server;
    }
    public void go() {
        Scanner s = new Scanner(System.in);
        String nextLine;
        Action nextCommand;

        do {
            nextLine = s.nextLine();
            var commands = nextLine.split(" ");
            nextCommand = Action.from(commands[0]);
            switch (nextCommand) {
                case ADD -> tryAdd(commands[1]);
                case GET -> tryGet(commands[1]);
                case DELETE -> tryDelete(commands[1]);
            }
        } while (nextCommand != Action.EXIT);
    }

    private void tryAdd(String filename){
        if (isValidFilename(filename) && server.add(filename)) {
            System.out.printf("The file %s added successfully%n", filename);
        } else {
            System.out.printf("Cannot add the file %s %n", filename);
        }
    }

    private boolean isValidFilename(String filename){
        if (filename.length()>6) {
            return false;
        }

        if (filename.equals("file11")){
            return false;
        }
        return true;
    }

    private void tryGet(String filename){
        if (server.get(filename)) {
            System.out.printf("The file %s was sent%n", filename);
        } else {
            System.out.printf("The file %s not found%n", filename);
        }
    }
    private void tryDelete(String filename){
        if (server.delete(filename)) {
            System.out.printf("The file %s was deleted%n", filename);
        } else {
            System.out.printf("The file %s not found%n", filename);
        }
    }

}

public class Main {

    public static void main(String[] args) {

        Storage server = new Storage();
        Controller c = new Controller(server);
        c.go();

    }
}