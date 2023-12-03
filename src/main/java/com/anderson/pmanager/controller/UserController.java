package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.RegisterDto;
import com.anderson.pmanager.dtos.TaskRecordDto;
import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.model.TaskModel;
import com.anderson.pmanager.model.UserModel;
import com.anderson.pmanager.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @PostMapping
    public ResponseEntity register(@RequestBody @Valid RegisterDto data) {
        if (this.userRepository.findByLogin(data.login()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        };

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.login(), encryptedPassword, data.role());

        this.userRepository.save(newUser);
        RegisterDto responseDto = new RegisterDto(
                newUser.getName(),
                newUser.getLogin(),
                "******",
                newUser.getRole()
        );

        return ResponseEntity.ok(responseDto);


    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody @Valid RegisterDto updatedUserData) {
        Optional<UserModel> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            UserModel existingUser = existingUserOptional.get();


            if (!existingUser.getLogin().equals(updatedUserData.login()) && userRepository.findByLogin(updatedUserData.login()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login already in use by another user.");
            }


            existingUser.setName(updatedUserData.name());
            existingUser.setLogin(updatedUserData.login());
            existingUser.setRole(updatedUserData.role());


            if (updatedUserData.password() != null && !updatedUserData.password().isEmpty()) {
                String encryptedPassword = new BCryptPasswordEncoder().encode(updatedUserData.password());
                existingUser.setPassword(encryptedPassword);
            }


            UserModel updatedUser = userRepository.save(existingUser);


            RegisterDto responseDto = new RegisterDto(
                    updatedUser.getName(),
                    updatedUser.getLogin(),
                    "*******",
                    updatedUser.getRole()
            );

            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        userRepository.delete(optionalUser.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted sucessfully.");
    }

}
