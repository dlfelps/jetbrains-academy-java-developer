package contacts;

import java.util.Optional;
import java.util.regex.Pattern;

public class PhoneNumber {

    private final String validNumber;

    private static final Pattern pattern =  Pattern.compile("^\\+?(\\(\\w+\\)|\\w+[ -]\\(\\w{2,}\\)|\\w+)([ -]\\w{2,})*");

    private PhoneNumber(String s) {
        validNumber = s;
    }

    @Override
    public String toString(){
        return validNumber;
    }

    public static Optional<PhoneNumber> tryFrom(String s){
        var matcher = pattern.matcher(s);
        if (matcher.matches()){
            return Optional.of(new PhoneNumber(s));
        } else {
            System.out.println("Wrong number format!");
            return Optional.empty();
        }
    }

}
