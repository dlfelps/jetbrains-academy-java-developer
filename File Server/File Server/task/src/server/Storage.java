package server;


import java.util.HashSet;

public class Storage {
    HashSet<String> files;

    public Storage(){
        files = new HashSet<>();
    }

    public boolean add(String filename){
        return files.add(filename);
    }

    public boolean delete(String filename){
        return files.remove(filename);
    }

    public boolean get(String filename){
        return files.contains(filename);
    }
}
