package org.oagi.srt.uat.testcase.phase17;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.phase2.TestCase2_Helper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase17_5 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs freeUser;

    @Before
    public void setUp() {
        loginAsAdmin(webDriver);

        freeUser = CreateAccountInputs.generateRandomly(random);
        TestCase2_Helper.createFreeAccount(webDriver, freeUser);

        logout(webDriver);
        login(webDriver, freeUser.getLoginId(), freeUser.getPassword());
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testFreeUserCanChangePassword() {
        gotoSubMenu(webDriver, "Admin", "Personal information");

        WebElement changePasswordButton = findElementByText(webDriver, "button[type=submit]", "Change a password");
        changePasswordButton.click();

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement oldPassword = findElementByContainingId(webDriver, "input[type=password]", "user_old_password");
        sendKeys(oldPassword, freeUser.getPassword());

        WebElement newPassword = findElementByContainingId(webDriver, "input[type=password]", "user_new_password");
        sendKeys(newPassword, updateAccountInfos.getPassword());

        WebElement confirmNewPassword = findElementByContainingId(webDriver, "input[type=password]", "user_confirm_new_password");
        sendKeys(confirmNewPassword, updateAccountInfos.getConfirmPassword());

        WebElement updatePasswordButton = findElementByText(webDriver, "button[type=submit]", "Update Password");
        updatePasswordButton.click();

        String detailMessage = getDetailMessage(webDriver);
        assertEquals(detailMessage, "Password changed successfully.");

        logout(webDriver);
        login(webDriver, freeUser.getLoginId(), updateAccountInfos.getPassword(), true);
    }

}
