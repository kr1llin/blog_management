package src.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import src.model.User;
import src.model.dto.RegisterRequest;
import src.model.dto.UserResponse;
import src.services.RegistrationService;
import src.services.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {
    UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable page
    ){
       Page<UserResponse> users = userService.getAll(page);
       return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
