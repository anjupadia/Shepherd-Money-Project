package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    @Autowired
    UserRepository userRepository;

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());
        userRepository.save(user);
        ResponseEntity<Integer> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }


    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        User user = userRepository.findById(userId).get();
        ResponseEntity<String> response;
        if (user != null) {
            userRepository.delete(user);
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
