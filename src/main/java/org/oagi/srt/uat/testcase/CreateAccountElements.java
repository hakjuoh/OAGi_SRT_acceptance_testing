package org.oagi.srt.uat.testcase;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

public class CreateAccountElements {

    private WebDriver webDriver;

    private WebElement loginIdElement;
    private WebElement nameElement;

    private WebElement userTypeElement;
    private WebElement userTypeDropdownElement;
    private WebElement userRoleElement;
    private WebElement userRoleDropdownElement;
    private WebElement enterpriseNameElement;
    private WebElement enterpriseNameDropdownElement;

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

    public void setUserTypeElement(WebElement userTypeElement, WebElement userTypeDropdownElement) {
        this.userTypeElement = userTypeElement;
        this.userTypeDropdownElement = userTypeDropdownElement;
    }

    public void sendUserType(UserType userType) {
        userTypeDropdownElement.click();

        String expectedUserType = userType.toString();
        clickDropdownElement(userTypeElement, expectedUserType);
    }

    private List<WebElement> findDropdownElements(String containsId) {
        List<WebElement> autocompletePanels = webDriver.findElements(By.cssSelector("div.ui-autocomplete-panel"));
        for (WebElement autocompletePanel : autocompletePanels) {
            String id = autocompletePanel.getAttribute("id");
            if (id.contains(containsId)) {
                return webDriver.findElements(By.cssSelector("div[id='" + id + "'] > ul.ui-autocomplete-items > li"));
            }
        }
        throw new IllegalStateException();
    }

    private void clickDropdownElement(WebElement element, String targetLabel) {
        String elementId = element.getAttribute("id");
        String containsId = elementId.substring(elementId.indexOf(':') + 1, elementId.length());

        WebElement inputTextElement = getInputTextElement(elementId);
        while (StringUtils.isEmpty(getInputTextElement(elementId).getAttribute("value"))) {
            List<WebElement> dropdownElements = findDropdownElements(containsId);
            for (WebElement dropdownElement : dropdownElements) {
                String itemLabel = dropdownElement.getAttribute("data-item-label");
                if (targetLabel.equals(itemLabel)) {
                    while (StringUtils.isEmpty(getInputTextElement(elementId).getAttribute("value"))) {
                        try {
                            dropdownElement.click();
                        } catch (ElementNotVisibleException ignore) {
                        }
                    }
                }
            }
        }
    }

    private WebElement getInputTextElement(String id) {
        return webDriver.findElement(By.cssSelector("span[id='" + id + "'] > input[type='text']"));
    }

    public WebElement getUserRoleElement() {
        return userRoleElement;
    }

    public void setUserRoleElement(WebElement userRoleElement, WebElement userRoleDropdownElement) {
        this.userRoleElement = userRoleElement;
        this.userRoleDropdownElement = userRoleDropdownElement;
    }

    public void sendUserRole(UserRole userRole) {
        userRoleDropdownElement.click();

        String expectedUserRole = userRole.toString();
        clickDropdownElement(userRoleElement, expectedUserRole);
    }

    public WebElement getEnterpriseNameElement() {
        return enterpriseNameElement;
    }

    public void setEnterpriseNameElement(WebElement enterpriseNameElement, WebElement enterpriseNameDropdownElement) {
        this.enterpriseNameElement = enterpriseNameElement;
        this.enterpriseNameDropdownElement = enterpriseNameDropdownElement;
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
