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
            System.out.printf("%d. %s%n",i+1, contacts.get(i).toString());
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
            System.out.print("Select a field:");
            var field = s.next();
            System.out.printf("Enter %s:", field);
            Scanner s2 = new Scanner(System.in);
            String newInput = s2.nextLine();

            var currentContact = contacts.get(id-1);
            var newContact = switch(field){
                case "name" -> currentContact.withFirst(newInput);
                case "surname" -> currentContact.withLast(newInput);
                default -> currentContact.withNumber(newInput);
            };

            contacts.set(id-1, newContact); // input is 1-based indexing
            System.out.println("The record updated!");

        }
        return true;
    }

    private boolean add(){
        Scanner s = new Scanner(System.in);

        System.out.printf("%nEnter the name:");
        String first = s.next();

        System.out.printf("%nEnter the surname:");
        String last = s.next();

        System.out.println("Enter the number:");
        Scanner s2 = new Scanner(System.in);
        String number = s2.nextLine();
        var optionalPhoneNumber = PhoneNumber.tryFrom(number);
        contacts.add(new Contact(first, last, optionalPhoneNumber));
        System.out.println("The record added.");
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
        System.out.print("Enter action (add, remove, edit, count, list, exit):");
        Scanner s = new Scanner(System.in);
        String action = s.next();
        return switch(action){
            case "add" -> add();
            case "remove" -> remove();
            case "edit" -> edit();
            case "count" -> count();
            case "list" -> list();
            default -> false;
        };
    }




}
