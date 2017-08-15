package org.oagi.srt.uat.testcase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CreateEnterpriseElements {

    private WebDriver webDriver;

    private WebElement enterpriseNameElement;
    private WebElement firstNameElement;
    private WebElement lastNameElement;
    private WebElement phoneElement;
    private WebElement addressElement;
    private WebElement emailElement;
    private Select purgeDurationInMonthsElement;
    private WebElement signedAgreementElement;

    public CreateEnterpriseElements(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getEnterpriseNameElement() {
        return enterpriseNameElement;
    }

    public void setEnterpriseNameElement(WebElement enterpriseNameElement) {
        this.enterpriseNameElement = enterpriseNameElement;
    }

    public WebElement getFirstNameElement() {
        return firstNameElement;
    }

    public void setFirstNameElement(WebElement firstNameElement) {
        this.firstNameElement = firstNameElement;
    }

    public WebElement getLastNameElement() {
        return lastNameElement;
    }

    public void setLastNameElement(WebElement lastNameElement) {
        this.lastNameElement = lastNameElement;
    }

    public WebElement getPhoneElement() {
        return phoneElement;
    }

    public void setPhoneElement(WebElement phoneElement) {
        this.phoneElement = phoneElement;
    }

    public WebElement getAddressElement() {
        return addressElement;
    }

    public void setAddressElement(WebElement addressElement) {
        this.addressElement = addressElement;
    }

    public WebElement getEmailElement() {
        return emailElement;
    }

    public void setEmailElement(WebElement emailElement) {
        this.emailElement = emailElement;
    }

    public Select getPurgeDurationInMonthsElement() {
        return purgeDurationInMonthsElement;
    }

    public void setPurgeDurationInMonthsElement(Select purgeDurationInMonthsElement) {
        this.purgeDurationInMonthsElement = purgeDurationInMonthsElement;
    }

    public WebElement getSignedAgreementElement() {
        return signedAgreementElement;
    }

    public void setSignedAgreementElement(WebElement signedAgreementElement) {
        this.signedAgreementElement = signedAgreementElement;
    }
}
