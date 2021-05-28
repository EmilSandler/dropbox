package com.backend.dropbox.controller;

import com.backend.dropbox.customExceptions.EmailExistException;
import com.backend.dropbox.customExceptions.InvalidEmailException;
import com.backend.dropbox.registration.RegistrationRequest;
import com.backend.dropbox.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RegistrationController Class implements an Rest Controller that
 * handles registration requests.
 **/


@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    /**
     * This is the main method which receives a POST
     * request.
     *
     * @param request is a RegistrationRequest object.
     * @see RegistrationRequest for more info about RegistrationRequest object.
     **/
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            registrationService.register(request);
        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmailExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
}
