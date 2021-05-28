package com.backend.dropbox.service;

import com.backend.dropbox.customExceptions.EmailExistException;
import com.backend.dropbox.customExceptions.InvalidEmailException;
import com.backend.dropbox.entity.User;
import com.backend.dropbox.model.UserRole;
import com.backend.dropbox.registration.EmailValidator;
import com.backend.dropbox.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final UserFileService userFileService;


    public void register(RegistrationRequest request) throws InvalidEmailException, EmailExistException {

        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new InvalidEmailException("Email not valid");
        }
        try {
            UserDetails tempUserDetails = userService.loadUserByUsername(request.getEmail());
            if (tempUserDetails != null) {
                throw new EmailExistException("Email exists");
            }
        } catch (UsernameNotFoundException e) {
        }

        try {
            userFileService.createFolder("", request.getEmail());
        } catch (NoSuchFileException e) {
        }

        userService.signUpUser(
                new User(request.getFirstName(), request.getLastName(),
                        request.getEmail(), request.getPassword(),
                        UserRole.USER.getGrantedAuthorities(),
                        true,
                        true,
                        true
                )
        );
    }

}
