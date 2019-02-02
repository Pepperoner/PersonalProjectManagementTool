package com.project.ppmtool.controller;

import com.project.ppmtool.entity.User;
import com.project.ppmtool.service.UserService;
import com.project.ppmtool.service.ValidationErrorService;
import com.project.ppmtool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private ValidationErrorService validationErrorService;
    private UserService userService;
    private UserValidator userValidator;

    @Autowired
    public UserController(ValidationErrorService validationErrorService, UserService userService, UserValidator userValidator) {
        this.validationErrorService = validationErrorService;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){

        userValidator.validate(user,result);

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
