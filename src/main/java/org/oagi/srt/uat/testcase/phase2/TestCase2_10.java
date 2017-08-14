package org.oagi.srt.uat.testcase.phase2;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountElements;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.UserType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_10 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @Test
    public void testCreateAccountWithoutMobileNo() throws InterruptedException {
        loginAsAdmin(webDriver);

        WebElement menu = findElementByText(webDriver, "ul.navbar-nav > li > a", "Admin");
        menu.click();

        WebElement submenu = findElementByText(webDriver, "ul.dropdown-menu > li > a", "Manage Right for All Users");
        submenu.click();

        WebElement createUserBtn = findElementByText(webDriver, "button", "Create a user");
        createUserBtn.click();

        CreateAccountElements createAccountElements = createAccountElementsOnAdminPage(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        logger.info("Attempting to create account using " + createAccountInputs);

        createAccountElements.getLoginIdElement().sendKeys(createAccountInputs.getLoginId());
        createAccountElements.getNameElement().sendKeys(createAccountInputs.getName());

        createAccountElements.sendUserType(UserType.Free);
        createAccountElements.sendUserRole(UserRole.Free);

        createAccountElements.getMobileNoElement().clear();
        // Omitting the mobile number intentionally
        // createAccountElements.getMobileNoElement().sendKeys(createAccountInputs.getMobileNo());
        createAccountElements.getEmailAddressElement().sendKeys(createAccountInputs.getEmailAddress());

        createAccountElements.getPasswordElement().sendKeys(createAccountInputs.getPassword());
        createAccountElements.getConfirmPasswordElement().sendKeys(createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.ui-messages-error-detail")));
        assertNotNull(errorMessageElement);

        String errorMessage = errorMessageElement.getText();
        assertTrue(!StringUtils.isEmpty(errorMessage));

        logger.info("Error Message: " + errorMessage);
    }
}
