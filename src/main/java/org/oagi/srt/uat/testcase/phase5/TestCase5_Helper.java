package org.oagi.srt.uat.testcase.phase5;

import org.oagi.srt.uat.testcase.CreateAccountElements;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.phase2.TestCase2_Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

public class TestCase5_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase2_Helper.class);

    public static CreateAccountInputs createAccountByEnterpriseAdmin(WebDriver webDriver, CreateAccountInputs createAccountInputs, UserRole userRole) {
        logger.info("Attempting to create account using " + createAccountInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Tenant’s Users");

        WebElement createUserBtn = findElementByText(webDriver, "button", "Add user");
        createUserBtn.click();

        CreateAccountElements createAccountElements = createAccountElementsOnEnterpriseAdminPage(webDriver);

        sendKeys(createAccountElements.getLoginIdElement(), createAccountInputs.getLoginId());
        sendKeys(createAccountElements.getNameElement(), createAccountInputs.getName());

        Select userRoleSelect = new Select(createAccountElements.getUserRoleElement());
        userRoleSelect.selectByValue(userRole.toString());

        sendKeys(createAccountElements.getAddressElement(), createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        sendKeys(createAccountElements.getMobileNoElement(), createAccountInputs.getMobileNo());
        sendKeys(createAccountElements.getEmailAddressElement(), createAccountInputs.getEmailAddress());

        sendKeys(createAccountElements.getPasswordElement(), createAccountInputs.getPassword());
        sendKeys(createAccountElements.getConfirmPasswordElement(), createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        return createAccountInputs;
    }
}
