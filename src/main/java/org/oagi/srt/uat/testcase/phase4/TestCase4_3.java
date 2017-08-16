package org.oagi.srt.uat.testcase.phase4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createDeveloper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase4_3 {

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs developer;

    @Before
    public void setUp() {
        developer = createDeveloper(webDriver, random);
        logout(webDriver);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testOAGIDeveloperCanChangePassword() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        WebElement changePasswordButton = findElementByText(webDriver, "button[type=submit]", "Change a password");
        changePasswordButton.click();

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement oldPassword = findElementByContainingId(webDriver, "input[type=password]", "user_old_password");
        sendKeys(oldPassword, developer.getPassword());

        WebElement newPassword = findElementByContainingId(webDriver, "input[type=password]", "user_new_password");
        sendKeys(newPassword, updateAccountInfos.getPassword());

        WebElement confirmNewPassword = findElementByContainingId(webDriver, "input[type=password]", "user_confirm_new_password");
        sendKeys(confirmNewPassword, updateAccountInfos.getConfirmPassword());

        WebElement updatePasswordButton = findElementByText(webDriver, "button[type=submit]", "Update Password");
        updatePasswordButton.click();

        logout(webDriver);
        login(webDriver, developer.getLoginId(), updateAccountInfos.getPassword());
    }


}
