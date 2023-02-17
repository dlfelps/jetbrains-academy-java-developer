import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {



    public static void main(String[] args) {
        var stat = IntStream.of().summaryStatistics();

        System.out.println(String.format("Count: %d, Sum: %d, Max: %d, Avg: %.1f",
                stat.getCount(), stat.getSum(), stat.getMax(), stat.getAverage()));
    }

    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}