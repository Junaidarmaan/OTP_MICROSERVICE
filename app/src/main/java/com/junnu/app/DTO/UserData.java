package com.junnu.app.DTO;

public class UserData {
    String email;
    String content;
    String subject;
    int expiry;
    String type;
    int length;
    
    
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
}
