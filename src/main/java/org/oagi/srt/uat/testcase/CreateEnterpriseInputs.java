package org.oagi.srt.uat.testcase;

import java.util.Random;

public class CreateEnterpriseInputs {
    
    private String enterpriseName;
    
    private String firstName;
    
    private String lastName;
    
    private String phone;
    
    private String address;
    
    private String emailAddress;
    
    private int purgeDurationInMonths;
    
    private boolean signedAgreement;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getPurgeDurationInMonths() {
        return purgeDurationInMonths;
    }

    public void setPurgeDurationInMonths(int purgeDurationInMonths) {
        this.purgeDurationInMonths = purgeDurationInMonths;
    }

    public boolean isSignedAgreement() {
        return signedAgreement;
    }

    public void setSignedAgreement(boolean signedAgreement) {
        this.signedAgreement = signedAgreement;
    }

    public static CreateEnterpriseInputs generateRandomly(Random random) {
        int randomNo = random.nextInt(10000000);

        String suffix = String.format("%08d", randomNo);
        CreateEnterpriseInputs createEnterpriseInputs = new CreateEnterpriseInputs();
        createEnterpriseInputs.setEnterpriseName("test_enterprise_" + suffix);
        createEnterpriseInputs.setFirstName("TestEnterprise " + suffix);
        createEnterpriseInputs.setLastName("TestEnterprise " + suffix);
        createEnterpriseInputs.setAddress("100 Bureau Dr., #" + String.format("%4d", random.nextInt(10000)) + ", Gaithersburg, MD 20899");

        createEnterpriseInputs.setPhone(String.format("%03d-%03d-%04d", random.nextInt(1000), random.nextInt(1000), random.nextInt(10000)));
        // Note: Does not support the mobile number formatting.
        createEnterpriseInputs.setPhone(createEnterpriseInputs.getPhone().replaceAll("-", ""));

        createEnterpriseInputs.setEmailAddress("testenterprise" + suffix + "@nist.gov");

        int randomMonth = 0;
        while (randomMonth == 0) {
            randomMonth = random.nextInt(13);
        }
        createEnterpriseInputs.setPurgeDurationInMonths(randomMonth);
        createEnterpriseInputs.setSignedAgreement(true);

        return createEnterpriseInputs;
    }

    @Override
    public String toString() {
        return "CreateEnterpriseInputs{" +
                "enterpriseName='" + enterpriseName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", purgeDurationInMonths='" + purgeDurationInMonths + '\'' +
                ", signedAgreement=" + signedAgreement +
                '}';
    }
}
