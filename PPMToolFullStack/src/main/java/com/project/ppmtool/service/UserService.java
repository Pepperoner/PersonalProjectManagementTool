package com.project.ppmtool.service;

import com.project.ppmtool.entity.User;
import com.project.ppmtool.exception.UserNameExistsException;
import com.project.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUserName(newUser.getUserName());
            //Make sure that pass and confirmPass is match
            //We mustnt persist or show confirmPass
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new UserNameExistsException("Username '"+newUser.getUserName()+"' already exists");
        }
    }
}
