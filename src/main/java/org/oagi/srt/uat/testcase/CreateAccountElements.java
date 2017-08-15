package org.oagi.srt.uat.testcase;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static org.oagi.srt.uat.testcase.TestCaseHelper.clickDropdownElement;

public class CreateAccountElements {

    private WebDriver webDriver;

    private WebElement loginIdElement;
    private WebElement nameElement;

    private WebElement userTypeElement;
    private WebElement userRoleElement;
    private WebElement enterpriseNameElement;

    private WebElement addressElement;
    private WebElement mobileNoElement;
    private WebElement emailAddressElement;

    private WebElement passwordElement;
    private WebElement confirmPasswordElement;

    public CreateAccountElements(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getLoginIdElement() {
        return loginIdElement;
    }

    public void setLoginIdElement(WebElement loginIdElement) {
        this.loginIdElement = loginIdElement;
    }

    public WebElement getNameElement() {
        return nameElement;
    }

    public void setNameElement(WebElement nameElement) {
        this.nameElement = nameElement;
    }

    public WebElement getUserTypeElement() {
        return userTypeElement;
    }

    public void setUserTypeElement(WebElement userTypeElement) {
        this.userTypeElement = userTypeElement;
    }

    public void sendUserType(UserType userType) {
        String expectedUserType = userType.toString();
        clickDropdownElement(webDriver, userTypeElement, expectedUserType);
    }

    public WebElement getUserRoleElement() {
        return userRoleElement;
    }

    public void setUserRoleElement(WebElement userRoleElement) {
        this.userRoleElement = userRoleElement;
    }

    public void sendUserRole(UserRole userRole) {
        String expectedUserRole = userRole.toString();
        clickDropdownElement(webDriver, userRoleElement, expectedUserRole);
    }

    public WebElement getEnterpriseNameElement() {
        return enterpriseNameElement;
    }

    public void setEnterpriseNameElement(WebElement enterpriseNameElement) {
        this.enterpriseNameElement = enterpriseNameElement;
    }

    public WebElement getAddressElement() {
        return addressElement;
    }

    public void setAddressElement(WebElement addressElement) {
        this.addressElement = addressElement;
    }

    public WebElement getMobileNoElement() {
        return mobileNoElement;
    }

    public void setMobileNoElement(WebElement mobileNoElement) {
        this.mobileNoElement = mobileNoElement;
    }

    public WebElement getEmailAddressElement() {
        return emailAddressElement;
    }

    public void setEmailAddressElement(WebElement emailAddressElement) {
        this.emailAddressElement = emailAddressElement;
    }

    public WebElement getPasswordElement() {
        return passwordElement;
    }

    public void setPasswordElement(WebElement passwordElement) {
        this.passwordElement = passwordElement;
    }

    public WebElement getConfirmPasswordElement() {
        return confirmPasswordElement;
    }

    public void setConfirmPasswordElement(WebElement confirmPasswordElement) {
        this.confirmPasswordElement = confirmPasswordElement;
    }
}
