package cz.niderle.server.auth.controller;

import cz.niderle.server.auth.dto.Users;
import cz.niderle.server.auth.exchange.JwtResponse;
import cz.niderle.server.auth.exchange.LoginRequest;
import cz.niderle.server.auth.exchange.RegisterRequest;
import cz.niderle.server.auth.service.UserService;
import cz.niderle.server.core.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        boolean created = userService.registerUser(request.getUsername(), request.getPassword());
        if (created) {
            return ResponseEntity.ok("Uživatel zaregistrován");
        } else {
            return ResponseEntity.badRequest().body("Uživatel již existuje");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Users user = userService.findByUsername(request.getUsername());
        if (user == null || !userService.checkPassword(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neplatné přihlašovací údaje");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
