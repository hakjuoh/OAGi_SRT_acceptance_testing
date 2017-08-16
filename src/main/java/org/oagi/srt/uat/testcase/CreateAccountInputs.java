package org.oagi.srt.uat.testcase;

import java.util.Base64;
import java.util.Random;

public class CreateAccountInputs {

    public static final CreateAccountInputs OAGI_ADMIN;
    static {
        OAGI_ADMIN = new CreateAccountInputs();
        OAGI_ADMIN.setLoginId("oagis");
        OAGI_ADMIN.setPassword("oagis");
    }

    private String loginId;
    private String name;
    private String address;
    private String mobileNo;
    private String emailAddress;
    private String password;
    private String confirmPassword;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public static CreateAccountInputs generateRandomly(Random random) {
        int randomNo = random.nextInt(10000000);

        String suffix = String.format("%08d", randomNo);
        CreateAccountInputs createAccountInputs = new CreateAccountInputs();
        createAccountInputs.setLoginId("testuser_" + suffix);
        createAccountInputs.setName("TestUser " + suffix);
        createAccountInputs.setAddress("100 Bureau Dr., #" + String.format("%4d", random.nextInt(10000)) + ", Gaithersburg, MD 20899");

        int phonePrefix = 0;
        while (phonePrefix < 100) {
            phonePrefix = random.nextInt(1000);
        }
        createAccountInputs.setMobileNo(String.format("%03d-%03d-%04d", phonePrefix, random.nextInt(1000), random.nextInt(10000)));
        // Note: Does not support the mobile number formatting.
        createAccountInputs.setMobileNo(createAccountInputs.getMobileNo().replaceAll("-", ""));

        createAccountInputs.setEmailAddress("testuser" + suffix + "@nist.gov");

        byte[] bytes = new byte[24];
        random.nextBytes(bytes);

        Base64.Encoder encoder = Base64.getEncoder();
        String password = new String(encoder.encode(bytes));
        createAccountInputs.setPassword(password);
        createAccountInputs.setConfirmPassword(password);

        return createAccountInputs;
    }

    @Override
    public String toString() {
        return "CreateAccountInputs{" +
                "loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
