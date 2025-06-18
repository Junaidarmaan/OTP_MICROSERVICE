package com.junnu.app.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserData {
    @Email
    @NotBlank(message = "cant leave the target email empty")
    String email;
    String content;
    String subject;
    @Min(value = 5 , message = "The minimum value for expiry time out must be more than equal 5 minutes")
    @Max(value = 60 , message = "The maximum value for expiry time out must not exceed 60 minutes")
    int expiry;
    String type;
    String appName = "Junnu Message";
    String filePath;
    @Min(value = 4,message = "The minimum length must be 4")
    @Max(value = 16 , message = "Sorry Length cant be more than 16")
    int length;
    boolean attachmentNeeded = true;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public int getExpiry() {
        return expiry;
    }
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    @Override
    public String toString() {
        return "UserData [email=" + email + ", content=" + content + ", subject=" + subject + ", expiry=" + expiry
                + ", type=" + type + ", length=" + length + "]";
    }
    
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public boolean isAttachmentNeeded() {
        return attachmentNeeded;
    }
    public void setAttachmentNeeded(boolean attachmentNeeded) {
        this.attachmentNeeded = attachmentNeeded;
    }
}
