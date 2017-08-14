package org.oagi.srt.uat.testcase.phase2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountElements;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.UserType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_2 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @Test
    public void testCreateAccountWithAllInformation() throws InterruptedException {
        loginAsAdmin(webDriver);

        WebElement menu = findElementByText(webDriver, "ul.navbar-nav > li > a", "Admin");
        menu.click();

        WebElement submenu = findElementByText(webDriver, "ul.dropdown-menu > li > a", "Manage Right for All Users");
        submenu.click();

        WebElement createUserBtn = findElementByText(webDriver, "button", "Create a user");
        createUserBtn.click();

        CreateAccountElements createAccountElements = createAccountElementsOnAdminPage(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        logger.info("Create Account using " + createAccountInputs);

        createAccountElements.getLoginIdElement().sendKeys(createAccountInputs.getLoginId());
        createAccountElements.getNameElement().sendKeys(createAccountInputs.getName());

        createAccountElements.sendUserType(UserType.Free);
        createAccountElements.sendUserRole(UserRole.Free);

        createAccountElements.getAddressElement().sendKeys(createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        createAccountElements.getMobileNoElement().sendKeys(createAccountInputs.getMobileNo());
        createAccountElements.getEmailAddressElement().sendKeys("hno2@nist.gov");

        createAccountElements.getPasswordElement().sendKeys(createAccountInputs.getPassword());
        createAccountElements.getConfirmPasswordElement().sendKeys(createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        logout(webDriver);
        login(webDriver, createAccountInputs.getLoginId(), createAccountInputs.getPassword());
    }
}
