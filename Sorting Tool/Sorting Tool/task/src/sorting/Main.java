package sorting;

import org.jetbrains.annotations.NotNull;

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

    public void printMe(){
        System.out.printf("%s: (%d time(s), %.0f%%)%n", key, count, 100*count.floatValue()/total);
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
}

class LongParser  {
    ArrayList<Long> tokens;

    public LongParser(SortingType order) {
        tokens = new ArrayList<>();
        parseInput();
        switch (order){
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
    }

    private void printByCount() {
        System.out.printf("Total numbers: %d%n", tokens.size());

        // create a Map<String, Integer> that maps words to counts
        Map<Long, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<Long>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(MyCounter<Long>::printMe);

    }

    private void printNatural() {
        System.out.printf("Total numbers: %d%n", tokens.size());
        System.out.print("Sorted data:");
        tokens.stream()
                .sorted()
                .forEach(word -> System.out.printf(" %s", word));
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            // write your code here
            tokens.add(number);
        }
    }
}

class LineParser  {
    ArrayList<String> tokens;

    public LineParser(SortingType order) {
        tokens = new ArrayList<>();
        parseInput();
        switch (order){
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
    }

    private void printByCount() {
        System.out.printf("Total lines: %d%n", tokens.size());

        // create a Map<String, Integer> that maps words to counts
        Map<String, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<String>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(MyCounter<String>::printMe);

    }

    private void printNatural() {
        System.out.printf("Total lines: %d%n", tokens.size());
        System.out.print("Sorted data:");
        tokens.stream()
                .sorted()
                .forEach(word -> System.out.printf("%s%n", word));
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            // write your code here
            tokens.add(next);
        }
    }
}

class WordParser  {
    ArrayList<String> tokens;

    public WordParser(SortingType order) {
        tokens = new ArrayList<>();
        parseInput();
        switch (order){
            case NATURAL -> printNatural();
            case BYCOUNT -> printByCount();
        }
    }

    private void printByCount() {
        System.out.printf("Total words: %d%n", tokens.size());

        // create a Map<String, Integer> that maps words to counts
        Map<String, Long> wordCounts = tokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // order by occurrence
        wordCounts.entrySet().stream()
                .map(entry -> new MyCounter<String>(entry.getKey(), entry.getValue(), tokens.size()))
                .sorted()
                .forEach(MyCounter<String>::printMe);

    }

    private void printNatural() {
        System.out.printf("Total words: %d%n", tokens.size());
        System.out.print("Sorted data:");
        tokens.stream()
                .sorted()
                .forEach(word -> System.out.printf(" %s", word));
    }

    public void parseInput(){
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            // write your code here
            tokens.add(next);
        }
    }
}


public class Main {
    public static void main(final String[] args) {

        DataType mode = null;
        SortingType order = null;

        if (args.length == 2){ // -sortingType is natural if not specified
            order = SortingType.NATURAL;
            mode = DataType.from(args[1]);
        } else { // length is 4
            if (args[0].equals("-sortingType")){ // sortingType then dataType
                order = SortingType.from(args[1]);
                mode = DataType.from(args[3]);
            } else { //dataType then sortingType
                order = SortingType.from(args[3]);
                mode = DataType.from(args[1]);
            }
        }

        switch (mode){
            case LONG -> new LongParser(order);
            case LINE -> new LineParser(order);
            case WORD -> new WordParser(order);
        }
    }
}
