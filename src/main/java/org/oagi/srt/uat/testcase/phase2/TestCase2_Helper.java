package org.oagi.srt.uat.testcase.phase2;

import org.oagi.srt.uat.testcase.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

public class TestCase2_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase2_Helper.class);

    public static void createFreeAccount(WebDriver webDriver, CreateAccountInputs createAccountInputs) {
        createAccount(webDriver, createAccountInputs, UserType.Free, UserRole.Free);
    }

    public static void createAccount(WebDriver webDriver, CreateAccountInputs createAccountInputs, UserType userType, UserRole userRole) {
        logger.info("Attempting to create account using " + createAccountInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        WebElement createUserBtn = findElementByText(webDriver, "button", "Create a user");
        createUserBtn.click();

        CreateAccountElements createAccountElements = createAccountElementsOnAdminPage(webDriver);

        sendKeys(createAccountElements.getLoginIdElement(), createAccountInputs.getLoginId());
        sendKeys(createAccountElements.getNameElement(), createAccountInputs.getName());

        createAccountElements.sendUserType(userType);
        createAccountElements.sendUserRole(userRole);

        sendKeys(createAccountElements.getAddressElement(), createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        sendKeys(createAccountElements.getMobileNoElement(), createAccountInputs.getMobileNo());
        sendKeys(createAccountElements.getEmailAddressElement(), createAccountInputs.getEmailAddress());

        sendKeys(createAccountElements.getPasswordElement(), createAccountInputs.getPassword());
        sendKeys(createAccountElements.getConfirmPasswordElement(), createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        // assertCreateAccount(webDriver, createAccountInputs.getLoginId());
    }

    public static void createEnterpriseAccount(WebDriver webDriver, CreateAccountInputs createAccountInputs, CreateEnterpriseInputs createEnterpriseInputs, UserRole userRole) {
        logger.info("Attempting to create enterprise account using " + createAccountInputs + ", " + createEnterpriseInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        WebElement createUserBtn = findElementByText(webDriver, "button", "Create a user");
        createUserBtn.click();

        CreateAccountElements createAccountElements = createAccountElementsOnAdminPage(webDriver);

        sendKeys(createAccountElements.getLoginIdElement(), createAccountInputs.getLoginId());
        sendKeys(createAccountElements.getNameElement(), createAccountInputs.getName());

        createAccountElements.sendUserType(UserType.Enterprise);
        createAccountElements.sendUserRole(userRole);
        createAccountElements.sendEnterpriseName(createEnterpriseInputs.getEnterpriseName());

        sendKeys(createAccountElements.getAddressElement(), createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        sendKeys(createAccountElements.getMobileNoElement(), createAccountInputs.getMobileNo());
        sendKeys(createAccountElements.getEmailAddressElement(), createAccountInputs.getEmailAddress());

        sendKeys(createAccountElements.getPasswordElement(), createAccountInputs.getPassword());
        sendKeys(createAccountElements.getConfirmPasswordElement(), createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        // assertCreateAccount(webDriver, createAccountInputs.getLoginId());
    }

    public static void assertCreateAccount(WebDriver webDriver, String loginId) {
        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        WebElement searchInputText = findElementByContainingId(webDriver, "span.ui-autocomplete > input[type=text]", "loginId");
        searchInputText.sendKeys(loginId);
        findElementByText(webDriver, "span.ui-autocomplete-query", loginId).click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS)) {
            try {
                webDriver.findElement(By.cssSelector("tbody > tr[data-ri='1'] > td"));
            } catch (NoSuchElementException e) {
                break;
            }
        }

        assertNotNull(findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", loginId));
    }
}
