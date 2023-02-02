package contacts;

import java.util.ArrayList;
import java.util.Scanner;

public class PhoneBook {
    private ArrayList<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public boolean count(){
        System.out.printf("The Phone Book has %d records%n", contacts.size());
        return true;
    }

    public boolean list(){
        for (int i=0; i<contacts.size();i++){
            System.out.printf("%d. %s",i+1, contacts.get(i).shortName());
        }
        return true;
    }

    private boolean edit(){
        if (contacts.isEmpty()){
            System.out.println("No records to edit!");
        } else{
            list();
            System.out.print("Select a record:");
            Scanner s = new Scanner(System.in);
            var id = s.nextInt();


            var currentContact = contacts.get(id-1); //gets reference to current id
            if (currentContact instanceof Person p) {
                System.out.print("Select a field (name, surname, birth, gender, number): ");
                var field = s.next();
                System.out.printf("Enter %s:", field);
                Scanner s2 = new Scanner(System.in);
                String newInput = s2.nextLine();

                switch(field){
                    case "name" -> p.editFirst(newInput);
                    case "surname" -> p.editLast(newInput);
                    case "birth" -> p.editDob(newInput);
                    case "gender" -> p.editGender(newInput);
                    default -> p.editNumber(newInput);
                };
            } else if (currentContact instanceof Organization o) {
                System.out.print("Select a field (address, number): ");
                var field = s.next();
                System.out.printf("Enter %s:", field);
                Scanner s2 = new Scanner(System.in);
                String newInput = s2.nextLine();

                switch(field){
                    case "address" -> o.editAddress(newInput);
                    default -> o.editNumber(newInput);
                };
            }

            System.out.printf("The record updated!%n%n");

        }
        return true;
    }

    private boolean add(){
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the type (person, organization): ");
        String type = s.next();

        switch (type){
            case "person" -> contacts.add(Person.getInput());
            default -> contacts.add(Organization.getInput());
        }
        System.out.printf("The record added.%n");
        return true;
    }

    private boolean remove(){
        if (contacts.isEmpty()){
            System.out.println("No records to remove!");
        } else {
            Scanner s = new Scanner(System.in);
            list();

            System.out.printf("%nSelect a record:");
            var id = s.nextInt();

            contacts.remove(id - 1);
            System.out.println("The record removed!");
        }
        return true;
    }

    public boolean menu(){
        System.out.print("Enter action (add, remove, edit, count, info, exit): ");
        Scanner s = new Scanner(System.in);
        String action = s.next();
        return switch(action){
            case "add" -> add();
            case "remove" -> remove();
            case "edit" -> edit();
            case "count" -> count();
            case "info" -> info();
            default -> false;
        };
    }

    private boolean info() {
        list();
        Scanner s = new Scanner(System.in);

        System.out.printf("%nSelect a record: ");
        var id = s.nextInt();

        System.out.print(contacts.get(id-1));
        return true;
    }


}
