package contacts;

import java.util.Optional;
import java.util.Scanner;

public class Contact {
    private String first;
    private String last;
    private Optional<PhoneNumber> number;

    public Contact(String first, String last, Optional<PhoneNumber> number){
        this.first = first;
        this.last = last;
        this.number = number;

    }


    public Contact withFirst(String first){
        return new Contact(first, this.last, this.number);
    }

    public Contact withLast(String last){
        return new Contact(this.first, last, this.number);
    }

    public Contact withNumber(String number){
        var optionalPhoneNumber = PhoneNumber.tryFrom(number);
        return new Contact(this.first, this.last, optionalPhoneNumber);
    }

    @Override
    public String toString(){
        var phone = number.map(PhoneNumber::toString).orElse("[no number]");
        return first + " " + last + ", " + phone;
    }
}
