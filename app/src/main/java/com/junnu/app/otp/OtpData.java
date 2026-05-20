package com.junnu.app.otp;

public class OtpData {
    private String otp;
    private Long expiry;
    private int tries;

    public OtpData(String otp, Long exp, int tries){
        this.otp = otp;
        this.expiry = exp;
        this.tries = tries;
    }
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }
    public String toString(){
        return "OTP: " + otp + " EXPIRY: " + expiry;
    }
    public int getTries() {
        return tries;
    }
    public void setTries(int tries) {
        this.tries = tries;
    }
}
