package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {

        System.out.println("Write how many cups of coffee you will need:");
        Scanner scanner = new Scanner(System.in);

        int numCups = scanner.nextInt();

        String text = """
                For %d cups of coffee you will need:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                """.formatted(numCups, 200*numCups, 50*numCups, 15*numCups);
        System.out.println(text);
    }
}
