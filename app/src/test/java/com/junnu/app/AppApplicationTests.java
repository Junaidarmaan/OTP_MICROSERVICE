package com.junnu.app;

import com.junnu.app.DTO.UserData;
import com.junnu.app.Services.MailService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private MailService mailService;

    @Test
    void sendMailTest() {
        UserData data = new UserData();
        data.setEmail("junnubest@gmail.com");
        data.setSubject("Test Subject");
        data.setContent("Your verification code is:");
        data.setAttachmentNeeded(false);
        data.setAppName("TestApp");
        data.setFilePath(""); 
        data.setType("alpha");
        data.setLength(111);
        data.setExpiry(5);
		mailService.sendEMail(data);
		
    }
}
