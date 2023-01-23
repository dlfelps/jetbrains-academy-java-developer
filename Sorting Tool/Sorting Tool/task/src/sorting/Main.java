package sorting;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

record MyCounter<T extends Comparable<T>>( T key, Long count, Integer total) implements Comparable<MyCounter<T>> {

    @Override
    public int compareTo(@NotNull MyCounter<T> c) {
        var comp = this.count.compareTo(c.count);
        if (comp != 0){
            return comp;
        } else {
            return this.key.compareTo(c.key);
        }
    }

    public String stringMe(){
        return String.format("%s: (%d time(s), %.0f%%)%n", key, count, 100*count.floatValue()/total);
    }
}

enum DataType {
    WORD,
    LONG,
    LINE;

    public static DataType from (String input) {
        return switch (input.toLowerCase()){
            case "word" -> WORD;
            case "long" -> LONG;
            case "line" -> LINE;
            default -> throw new IllegalArgumentException("Not a valid datatype");
        };
    }

    public static Optional<DataType> tryFrom (String input) {
        return switch (input.toLowerCase()){
            case "word" -> Optional.of(WORD);
            case "long" -> Optional.of(LONG);
            case "line" -> Optional.of(LINE);
            default -> Optional.empty();
        };
    }
}

enum SortingType {
    NATURAL,
    BYCOUNT;

    public static SortingType from (String input) {
        return switch (input.toLowerCase()){
            case "natural" -> NATURAL;
            case "bycount" -> BYCOUNT;
            default -> throw new IllegalArgumentException("Not a valid datatype");
        };
    }

    public static Optional<SortingType> tryFrom(String input) {
        return switch (input.toLowerCase()){
            case "natural" -> Optional.of(NATURAL);
            case "bycount" -> Optional.of(BYCOUNT);
            default -> Optional.empty();
        };
    }
}

class LongParser  {
    ArrayList<Long> tokens;
    Scanner scanner;
    Writer writer;

    public LongParser(SortingType order, Scanner scanner, Writer writer) throws IOException{

        tokens = new ArrayList<>();
        this.scanner = scanner;
        this.writer = writer;
        parseInput();
        switch (order) {
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
        writer.flush();
        writer.close();

    }

    private void writeMe(String s)  {
        try {
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void printByCount() throws IOException{
        writeMe(String.format("Total numbers: %d%n", tokens.size()));

        // create a Map<String, Integer> that maps words to counts
        Map<Long, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<Long>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(counter -> writeMe(counter.stringMe()));

    }

    private void printNatural() throws IOException{
        writeMe(String.format("Total numbers: %d%n", tokens.size()));
        writeMe(String.format("Sorted data:"));
        tokens.stream()
                .sorted()
                .forEach(word -> writeMe(String.format(" %s", word)));
    }

    public void parseInput(){

        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            // write your code here
            tokens.add(number);
        }

        writeMe("This is a test");

    }
}

class LineParser  {
    ArrayList<String> tokens;
    Scanner scanner;
    Writer writer;

    public LineParser(SortingType order, Scanner scanner, Writer writer) throws IOException {
        writeMe("Hello world");

        tokens = new ArrayList<>();
        this.scanner = scanner;
        this.writer = writer;
        parseInput();
        switch (order){
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
        writer.flush();
        writer.close();
    }

    private void writeMe(String s)  {
        try {
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void printByCount() throws IOException{
        writeMe(String.format("Total lines: %d%n", tokens.size()));

        // create a Map<String, Integer> that maps words to counts
        Map<String, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<String>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(counter -> writeMe(counter.stringMe()));

    }

    private void printNatural() throws IOException{
        writeMe(String.format("Total lines: %d%n", tokens.size()));
        writeMe(String.format("Sorted data:"));
        tokens.stream()
                .sorted()
                .forEach(word -> writeMe(String.format("%s%n", word)));
    }

    public void parseInput(){

        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            // write your code here
            tokens.add(next);
        }
    }
}

class WordParser  {
    ArrayList<String> tokens;
    Writer writer;
    Scanner scanner;

    public WordParser(SortingType order, Scanner scanner, Writer writer) throws IOException{
        tokens = new ArrayList<>();
        this.scanner = scanner;
        this.writer = writer;
        parseInput();
        switch (order){
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
        writer.flush();
        writer.close();
    }

    private void writeMe(String s)  {
        try {
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void printByCount() throws IOException{
        writeMe(String.format("Total words: %d%n", tokens.size()));

        // create a Map<String, Integer> that maps words to counts
        Map<String, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<String>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(counter -> writeMe(counter.stringMe()));

    }

    private void printNatural() throws IOException{
        writeMe(String.format("Total words: %d%n", tokens.size()));
        writeMe(String.format("Sorted data:"));
        tokens.stream()
                .sorted()
                .forEach(word -> writeMe(String.format(" %s", word)));
    }

    public void parseInput(){
        while (scanner.hasNext()) {
            String next = scanner.next();
            // write your code here
            tokens.add(next);
        }
    }
}

class CheckArgs {

    HashMap<String, Integer> myMap;
    HashMap<Integer, String> reverseMap;


    CheckArgs(String[] args) {
        myMap = new HashMap<>();
        reverseMap = new HashMap<>();

        for (int i = 0; i< args.length; i++) {
            myMap.put(args[i], i);
            reverseMap.put(i, args[i]);
        }

    }

    public boolean validateArgs(){

        var dataType = getDataType();
        var sortingType = getSortingType();

        if (dataType.isEmpty()){
            System.out.println("No data type defined!");
            return false;
        }

        if (sortingType.isEmpty()){
            System.out.println("No sorting type defined!");
            return false;
        }

        return true;
    }

    public void checkExtraArgs() {

        // should only be called AFTER other checks
        // other keys should be present
        int loc = myMap.get("-dataType");
        reverseMap.remove(loc);
        reverseMap.remove(loc+1);

        if (myMap.containsKey("-sortingType")){ // may not contain sortingType
            int loc2 = myMap.get("-sortingType");
            reverseMap.remove(loc2);
            reverseMap.remove(loc2+1);
        }

        if (myMap.containsKey("-inputFile")){ // may not contain sortingType
            int loc2 = myMap.get("-inputFile");
            reverseMap.remove(loc2);
            reverseMap.remove(loc2+1);
        }

        if (myMap.containsKey("-outputFile")){ // may not contain sortingType
            int loc2 = myMap.get("-outputFile");
            reverseMap.remove(loc2);
            reverseMap.remove(loc2+1);
        }

        if (!reverseMap.isEmpty()) {
            reverseMap.entrySet().stream()
                    .forEach(entry ->
                            System.out.printf("\"%s\" is not a valid parameter. It will be skipped.", entry.getValue()));
        }
    }


    public Optional<DataType> getDataType(){
        if (!myMap.containsKey("-dataType")){
            return Optional.empty();
        }

        int loc = myMap.get("-dataType");
        var nextArg = reverseMap.getOrDefault(loc+1, "No data type defined!");
        return DataType.tryFrom(nextArg);
    }

    public Optional<SortingType> getSortingType(){
        if (!myMap.containsKey("-sortingType")){
            return Optional.of(SortingType.NATURAL);
        }

        int loc = myMap.get("-sortingType");
        var nextArg = reverseMap.getOrDefault(loc+1, "No sorting type defined!");
        return SortingType.tryFrom(nextArg);
    }

    public Optional<Writer> getWriter(){
        if (!myMap.containsKey("-outputFile")){ // default to console
            return Optional.of(new PrintWriter(System.out, true));
        }

        int loc = myMap.get("-outputFile");
        var nextArg = reverseMap.getOrDefault(loc+1, "File not found!");
        try {
            return Optional.of(new FileWriter(new File(nextArg)));
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return Optional.empty();
    }

    public Optional<Scanner> getScanner(){
        if (!myMap.containsKey("-inputFile")){ // default to console
            return Optional.of(new Scanner(System.in));
        }

        int loc = myMap.get("-inputFile");
        var nextArg = reverseMap.getOrDefault(loc+1, "File not found!");
        try {
            return Optional.of(new Scanner(new File(nextArg)));
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return Optional.empty();
    }
}
public class Main {
    public static void main(final String[] args) throws IOException{

        DataType mode = null;
        SortingType order = null;
        Scanner scanner = null;
        Writer writer = null;

        var checkArgs = new CheckArgs(args);
        if (!checkArgs.validateArgs()){
            return;
        }

        //check for extra args to be ignored
        checkArgs.checkExtraArgs();

        var realArgs = new CheckArgs(args);
        mode = realArgs.getDataType().get(); //guaranteed to be valid here
        order = realArgs.getSortingType().get();
        scanner = realArgs.getScanner().get();
        writer = realArgs.getWriter().get();

        switch (mode) {
            case LONG -> new LongParser(order, scanner, writer);
            case LINE -> new LineParser(order, scanner, writer);
            case WORD -> new WordParser(order, scanner, writer);
        }

    }
}
