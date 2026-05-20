package com.junnu.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.junnu.app.dto.NotificationRequest;
import com.junnu.app.notification.NotificationInterface;
import com.junnu.app.notification.ServiceFactory;
import com.junnu.app.otp.Store;

import jakarta.validation.Valid;

@RestController
public class HomeController {
    @Autowired
    ServiceFactory factory;

    @Autowired
    Store redis;

    @PostMapping("/sendotp")
    public ResponseEntity<String> sendOTP(@RequestBody @Valid NotificationRequest req) {
        NotificationInterface service = factory.getService(req.getChannel());
        System.out.println(service.getClass().toString());
        service.handleRequest(req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("MAIL WILL BE SENT SHORTLY..");
    }

    @PostMapping("/validateotp")
    public ResponseEntity<String> validateOTP(@RequestBody Map<String, String> request) {
        String user = request.get("user");
        String otp = request.get("otp");
        System.out.println("RECEIVED OTP VALIDATION REQUEST FOR USER: " + user + " WITH OTP: " + otp);
        boolean isValid = redis.isValid(user, otp);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body("OTP is valid");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP is invalid or expired");
    }

    @GetMapping("/allotps")
    public ResponseEntity<Object> getAllOTPs() {
        return ResponseEntity.status(HttpStatus.OK).body(redis.getAll());
    }
}
