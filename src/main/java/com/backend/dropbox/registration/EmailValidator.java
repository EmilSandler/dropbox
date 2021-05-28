package com.backend.dropbox.registration;


import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email Validation class for validation of email address.
 *
 * @author Emil Sandler
 **/


@Service
public class EmailValidator implements Predicate<String> {

    private static final String emailPatternRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @Override
    public boolean test(String email) {
        Pattern pattern = Pattern.compile(emailPatternRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

