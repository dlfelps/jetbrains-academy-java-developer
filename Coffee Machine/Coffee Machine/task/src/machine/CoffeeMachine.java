package machine;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Write how ml of water the coffee machine has:");
        int water = scanner.nextInt();
        System.out.println("Write how ml of milk the coffee machine has:");
        int milk = scanner.nextInt();
        System.out.println("Write how grams of coffee beans the coffee machine has:");
        int coffee = scanner.nextInt();
        System.out.println("Write how many cups of coffee you will need:");
        Integer numCups = scanner.nextInt();

        Integer numPossible = calculatePossible(water, milk, coffee);

        switch (numCups.compareTo(numPossible)) {
            case 1:
                System.out.println("No, I can only make %d cup(s) of coffee".formatted(numPossible));
                break;
            case 0:
                System.out.println("Yes, I can make that amount of coffee");
                break;
            case -1:
                System.out.println("Yes, I can make that amount of coffee (and even %d more than that)".formatted(numPossible-numCups));
                break;
        }
    }

    private static int calculatePossible(int water, int milk, int coffee) {
        Integer[] ingredients = {water/200, milk/50, coffee/15};
        return Collections.min(Arrays.asList(ingredients));

    }
}
