package org.example.portfoliomanager.controller;

import org.example.portfoliomanager.dto.DTOMapper;
import org.example.portfoliomanager.dto.ResponseAPI;
import org.example.portfoliomanager.dto.UserDTO;
import org.example.portfoliomanager.models.User;
import org.example.portfoliomanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<ResponseAPI<UserDTO>> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            UserDTO userDTO = DTOMapper.toUserDTO(savedUser);
            ResponseAPI<UserDTO> response = new ResponseAPI<>(true, "User created successfully.", userDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseAPI<>(false, "Invalid input: " + ex.getMessage(), null));
        } catch (Exception ex) {

            return ResponseEntity.status(500)
                    .body(new ResponseAPI<>(false, ex.getMessage(), null));
        }
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDTO userDTO = DTOMapper.toUserDTO(user);
            ResponseAPI<UserDTO> response = new ResponseAPI<>(true, "User fetched successfully.", userDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseAPI<>(false, "Error: " + ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(new ResponseAPI<>(false, "An unexpected error occurred.", null));
        }
    }

    // Get a user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<ResponseAPI<UserDTO>> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            UserDTO userDTO = DTOMapper.toUserDTO(user);
            ResponseAPI<UserDTO> response = new ResponseAPI<>(true, "User fetched successfully.", userDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new ResponseAPI<>(false, "Error: " + ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(new ResponseAPI<>(false, "An unexpected error occurred.", null));
        }
    }
}


