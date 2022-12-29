package machine;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

record CoffeeState(int water, int milk, int coffee, int cups, int money) {
    public static CoffeeState buyEspresso(CoffeeState initial) {
        return new CoffeeState(initial.water - 250, initial.milk, initial.coffee - 16, initial.cups - 1, initial.money + 4);
    }

    public static CoffeeState buyLatte(CoffeeState initial) {
        return new CoffeeState(initial.water - 350, initial.milk - 75, initial.coffee - 20, initial.cups - 1, initial.money + 7);
    }

    public static CoffeeState buyCappuccino(CoffeeState initial) {
        return new CoffeeState(initial.water - 200, initial.milk - 100, initial.coffee - 12, initial.cups - 1, initial.money + 6);
    }

    public static CoffeeState addWater(CoffeeState initial, int amount) {
        return new CoffeeState(initial.water + amount, initial.milk, initial.coffee, initial.cups, initial.money);
    }
    public static CoffeeState addMilk(CoffeeState initial, int amount) {
        return new CoffeeState(initial.water, initial.milk + amount, initial.coffee, initial.cups, initial.money);
    }
    public static CoffeeState addCoffee(CoffeeState initial, int amount) {
        return new CoffeeState(initial.water , initial.milk, initial.coffee+ amount, initial.cups, initial.money);
    }
    public static CoffeeState addCups(CoffeeState initial, int amount) {
        return new CoffeeState(initial.water, initial.milk, initial.coffee, initial.cups + amount, initial.money);
    }
    public static CoffeeState takeMoney(CoffeeState initial) {
        System.out.println("I gave you $%d".formatted(initial.money));
        return new CoffeeState(initial.water, initial.milk, initial.coffee, initial.cups , 0);

    }




    public static void printState(CoffeeState state) {
        String output = """
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """.formatted(state.water, state.milk, state.coffee, state.cups, state.money);
        System.out.println(output);
    }


}

public class CoffeeMachine {
    public static void main(String[] args) {

        CoffeeState state = new CoffeeState(400, 540, 120, 9, 550);
        Scanner scanner = new Scanner(System.in);

        String input;
        Integer buyChoice;

        CoffeeState.printState(state);

        System.out.println("Write action (buy, fill, take):");
        input = scanner.next();
        switch (input) {
            case "buy":
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                buyChoice = scanner.nextInt();
                switch (buyChoice.compareTo(2)) {
                    case 1: //cappuccino
                        state = CoffeeState.buyCappuccino(state);
                        break;
                    case 0: // latte
                        state = CoffeeState.buyLatte(state);
                        break;
                    case -1: //espresso
                        state = CoffeeState.buyEspresso(state);
                        break;
                }
                break;
            case "fill":
                System.out.println("Write how many ml of water you want to add:");
                state = CoffeeState.addWater(state, scanner.nextInt());

                System.out.println("Write how many ml of milk you want to add:");
                state = CoffeeState.addMilk(state, scanner.nextInt());

                System.out.println("Write how many grams of coffee you want to add:");
                state = CoffeeState.addCoffee(state, scanner.nextInt());

                System.out.println("Write how many disposable cups you want to add:");
                state = CoffeeState.addCups(state, scanner.nextInt());
                break;
            case "take":
                state = CoffeeState.takeMoney(state);
                break;
        }
        CoffeeState.printState(state);
    }



    private static int calculatePossible(int water, int milk, int coffee) {
        Integer[] ingredients = {water/200, milk/50, coffee/15};
        return Collections.min(Arrays.asList(ingredients));

    }
}
