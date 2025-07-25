package com.junnu.app.Controllers;

import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.junnu.app.DTO.UserData;
import com.junnu.app.Services.CredentialsService;
import com.junnu.app.Services.MailService;
import com.junnu.app.Services.StatisticsServices;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class HomeController {
    @Autowired
    MailService service;

    @Autowired
    CredentialsService cred;

    @Autowired
    StatisticsServices stats;

    @PostMapping("/sendotp")
    public ResponseEntity<String> sendOTP(@RequestParam(value = "file", required = false) MultipartFile file,
                                          @RequestBody @Valid UserData data,
                                          HttpServletRequest request) {
        try {
            // Create uploads directory
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save uploaded file if present
            if (file != null && !file.isEmpty()) {
                String filePath = uploadDir + file.getOriginalFilename();
                file.transferTo(new File(filePath));
                System.out.println("Uploaded File Path: " + filePath);
                data.setFilePath(filePath);
            }

            // Print client IP
            String ip = request.getRemoteAddr();
            System.out.println("IP ADDRESS OF CLIENT IS : " + ip);

            return service.sendEMail(data);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/validate/{otp}/{mail}")
    public ResponseEntity<String> validateOTP(@PathVariable String otp, @PathVariable String mail) {
        return cred.isValid(otp, mail);
    }

    @GetMapping("/success")
    public float getSuccessRate() {
        return stats.getSuccessRate();
    }

    @GetMapping("/failure")
    public float getFailureRate() {
        return stats.getFailureRate();
    }

    @GetMapping("/expired")
    public float getExpiredRate() {
        return stats.getExpiredRate();
    }

    @GetMapping("/invalid")
    public float getInvalidRate() {
        return stats.getInvalidRate();
    }
}
