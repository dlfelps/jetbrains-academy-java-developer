package contacts;


public class Main {
    public static void main(String[] args) {

    PhoneBook pb = new PhoneBook();
    var menu = true;
    while (menu){
        menu = pb.menu();
        System.out.println();
    }
    }
}
