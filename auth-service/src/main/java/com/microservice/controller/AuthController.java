package com.microservice.controller;


import com.microservice.dto.LoginRequest;
import com.microservice.entity.User;
import com.microservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        System.out.println("registering into our application");
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        return authService.login(req.getUsername(), req.getPassword());
    }

    @GetMapping("/get-customer-id")
    public Long getCustomerId(@RequestParam String username) {
        System.out.println("Received username = " + username);
        Long id = authService.getCustomerId(username);
        System.out.println("Returning customerId = " + id);
        return id;
    }

}
