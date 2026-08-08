package src.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.model.dto.JwtResponse;
import src.model.dto.LoginRequest;
import src.model.dto.RegisterRequest;
import src.proxies.JwtTokenProvider;
import src.services.RegistrationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegistrationService regService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        regService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registered");
    }

    // authenticate and generate token
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user request!");
        }
        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
