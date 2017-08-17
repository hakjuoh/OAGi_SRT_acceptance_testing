package org.oagi.srt.uat.testcase.phase8;

import org.oagi.srt.uat.testcase.CreateAccountElements;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

public class TestCase8_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase8_Helper.class);

    public static void createAccount(WebDriver webDriver, CreateAccountInputs createAccountInputs) {
        logger.info("Attempting to create account using " + createAccountInputs);

        WebElement createAccountLink = findElementByText(webDriver, "a", "Create an account");
        createAccountLink.click();

        CreateAccountElements createAccountElements = createAccountElementsOnIndexPage(webDriver);

        sendKeys(createAccountElements.getLoginIdElement(), createAccountInputs.getLoginId());
        sendKeys(createAccountElements.getNameElement(), createAccountInputs.getName());

        sendKeys(createAccountElements.getAddressElement(), createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        sendKeys(createAccountElements.getMobileNoElement(), createAccountInputs.getMobileNo());
        sendKeys(createAccountElements.getEmailAddressElement(), createAccountInputs.getEmailAddress());

        sendKeys(createAccountElements.getPasswordElement(), createAccountInputs.getPassword());
        sendKeys(createAccountElements.getConfirmPasswordElement(), createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();
    }
}
