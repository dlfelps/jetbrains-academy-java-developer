package contacts;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

sealed public interface Contact permits Person, Organization {
    public String shortName();
}

enum Gender {
    M, F;
}


final class Person implements Contact {
    private String first;
    private String last;
    private Optional<PhoneNumber> number;
    private Optional<Date> dob;
    private Optional<Gender> gender;
    private String created;
    private String edited;

    public Person(String first, String last, Optional<PhoneNumber> number, Optional<Date> dob, Optional<Gender> gender ){
        this.first = first;
        this.last = last;
        this.number = number;
        this.dob = dob;
        this.gender = gender;

        //Get formatted String
        created = getCurrenTime();
        edited = created;
    }

    public static String getCurrenTime(){
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(ZoneId.systemDefault());;

        //Local time instance
        Instant instant = Instant.now();

        //Get formatted String
        return FOMATTER.format(instant);

    }
    public static Contact getInput() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the name: ");
        String first = s.next();

        System.out.print("Enter the surname: ");
        String last = s.next();

        System.out.print("Enter the birthdate: ");
        Scanner s2 = new Scanner(System.in);
        String sDob = s2.nextLine();
        var dob = optionalDateFromString(sDob);

        System.out.print("Enter the gender: ");
        String gstring = s2.nextLine();
        var gender = optionalGenderFromString(gstring);

        System.out.print("Enter the number: ");
        var number = PhoneNumber.tryFrom(s2.nextLine());

        return new Person(first, last, number, dob, gender);
    }

    private static Optional<Date> optionalDateFromString(String sDob) {
        Optional<Date> dob;
        try {
            dob = Optional.of(DateFormat.getDateInstance().parse(sDob));
        }catch (ParseException e) {
            System.out.println("Bad birth date!");
            dob = Optional.empty();
        }
        return dob;
    }

    private static Optional<Gender> optionalGenderFromString(String gstring) {
        Optional<Gender> gender;

        try {
            gender = Optional.of(Gender.valueOf(gstring));
        }catch (IllegalArgumentException e) {
            System.out.println("Bad gender!");
            gender = Optional.empty();
        }
        return gender;
    }


    public void editFirst(String first){
        this.first = first;
        this.edited = getCurrenTime();
    }
    public void editLast(String last){
        this.last = last;
        this.edited = getCurrenTime();
    }
    public void editNumber(String number){
        this.number = PhoneNumber.tryFrom(number);
        this.edited = getCurrenTime();
    }
    public void editDob(String dob){
        this.dob = optionalDateFromString(dob);
        this.edited = getCurrenTime();
    }

    public void editGender(String gstring){
        this.gender = optionalGenderFromString(gstring);
        this.edited = getCurrenTime();
    }

    @Override
    public String toString(){
        var sNumber = number.map(PhoneNumber::toString).orElse("[no data]");
        var sGender =  gender.map(Gender::toString).orElse("[no data]");
        var sDob =  dob.map(Date::toString).orElse("[no data]");
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + first + System.lineSeparator());
        sb.append("Surname: " + last + System.lineSeparator());
        sb.append("Birth date: " + sDob + System.lineSeparator());
        sb.append("Gender: " + sGender + System.lineSeparator());
        sb.append("Number: " + sNumber + System.lineSeparator());
        sb.append("Time created: " + created + System.lineSeparator());
        sb.append("Time last edit: " + edited + System.lineSeparator());
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    public String shortName(){
        return first + " " + last;
    }
}

final class Organization implements Contact {
    private String name;
    private String address;
    private Optional<PhoneNumber> number;

    private String created;
    private String edited;

    public Organization(String name, String address, Optional<PhoneNumber> number){
        this.name = name;
        this.address = address;
        this.number = number;

        //Get formatted String
        created = getCurrenTime();
        edited = created;

    }

    public static String getCurrenTime(){
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(ZoneId.systemDefault());;

        //Local time instance
        Instant instant = Instant.now();

        //Get formatted String
        return FOMATTER.format(instant);

    }

    public static Contact getInput() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the organization name:");
        String name = s.nextLine();

        System.out.print("Enter the address:");
        Scanner s2 = new Scanner(System.in);
        String address = s2.nextLine();

        System.out.print("Enter the number:");
        var number = PhoneNumber.tryFrom(s2.nextLine());

        return new Organization(name, address, number);
    }


    @Override
    public String toString(){
        var sNumber =  number.map(PhoneNumber::toString).orElse("[no data]");
        StringBuilder sb = new StringBuilder();
        sb.append("Organization name: " + name + System.lineSeparator());
        sb.append("Address: " + address + System.lineSeparator());
        sb.append("Number: " + sNumber + System.lineSeparator());
        sb.append("Time created: " + created + System.lineSeparator());
        sb.append("Time last edit: " + edited + System.lineSeparator());
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    public String shortName(){
        return name;
    }


    public void editName(String name){
        this.name = name;
        this.edited = getCurrenTime();
    }
    public void editAddress(String address){
        this.address = address;
        this.edited = getCurrenTime();
    }
    public void editNumber(String number){
        this.number = PhoneNumber.tryFrom(number);
        this.edited = getCurrenTime();
    }

}
