package sorting;

import java.util.*;


enum DataType {
    WORD,
    LONG,
    LINE,
    SORT;

    public static DataType from (String input) {
        return switch (input.toLowerCase()){
            case "word" -> WORD;
            case "long" -> LONG;
            case "line" -> LINE;
            default -> throw new IllegalArgumentException("Not a valid datatype");
        };
    }
}

abstract class Parser {

    ArrayList<Object> tokens;

    abstract void parseInput(); // fills tokens
    abstract void printOutput(); //prints output

}

class LongParser extends Parser {

    ArrayList<Long> tokens;

    public LongParser() {
        tokens = new ArrayList<>();
        parseInput();
        printOutput();
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            // write your code here
            tokens.add(number);
        }
    }

    public void printOutput(){
        var largest = Collections.max(tokens);
        var counter = tokens.stream().filter(number -> number == largest).count();

        System.out.printf("Total numbers: %d%n", tokens.size());
        System.out.printf("The greatest number: %d (%d time(s), %.0f%%)%n", largest, counter, 100*(Long.valueOf(counter).floatValue())/tokens.size());
    }
}

class LineParser extends Parser {

    ArrayList<String> tokens;

    public LineParser() {
        tokens = new ArrayList<>();
        parseInput();
        printOutput();
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            // write your code here
            tokens.add(next);
        }
    }

    public void printOutput(){
        var largest = Collections.max(tokens, Comparator.comparing(String::length));
        var counter = tokens.stream().filter(number -> number == largest).count();

        System.out.printf("Total lines: %d%n", tokens.size());
        System.out.println("The longest line:");
        System.out.println(largest);
        System.out.printf("(%d time(s), %.0f%%)%n", counter, 100*Long.valueOf(counter).floatValue()/tokens.size());
    }
}

class WordParser extends Parser {

    ArrayList<String> tokens;

    public WordParser() {
        tokens = new ArrayList<>();
        parseInput();
        printOutput();
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            // write your code here
            tokens.add(next);
        }
    }

    public void printOutput(){
        var largest = Collections.max(tokens, Comparator.comparing(String::length));
        var counter = tokens.stream().filter(number -> number == largest).count();

        System.out.printf("Total words: %d%n", tokens.size());
        System.out.printf("The longest word: %s (%d time(s), %.0f%%)%n", largest, counter, 100*Long.valueOf(counter).floatValue()/tokens.size());
    }
}

class SortParser extends Parser {

    ArrayList<Long> tokens;

    public SortParser() {
        tokens = new ArrayList<>();
        parseInput();
        printOutput();
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            // write your code here
            tokens.add(number);
        }
    }

    public void printOutput(){
        Collections.sort(tokens);
        System.out.printf("Total numbers: %d%n", tokens.size());
        System.out.print("Sorted data:");
        tokens.forEach(token -> System.out.printf(" %d", token));
    }
}

public class Main {
    public static void main(final String[] args) {

        DataType mode = null;

        for (String input : args) {
            if (input.equals("-sortIntegers")){
                mode = DataType.SORT;
            }
        }

        if (mode == null) {
            mode = DataType.from(args[1]);
        }

        switch (mode){
            case LONG -> new LongParser();
            case LINE -> new LineParser();
            case WORD -> new WordParser();
            case SORT -> new SortParser();
        }


    }
}
