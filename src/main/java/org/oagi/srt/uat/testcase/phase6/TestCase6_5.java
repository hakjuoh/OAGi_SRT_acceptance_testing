package org.oagi.srt.uat.testcase.phase6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase6_5 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin;
    private CreateAccountInputs enterpriseEndUser;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        enterpriseEndUser = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, enterpriseEndUser);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testEnterpriseEndUserCanChangePassword() {
        gotoSubMenu(webDriver, "Admin", "Personal information");

        WebElement changePasswordButton = findElementByText(webDriver, "button[type=submit]", "Change a password");
        changePasswordButton.click();

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement oldPassword = findElementByContainingId(webDriver, "input[type=password]", "user_old_password");
        sendKeys(oldPassword, enterpriseEndUser.getPassword());

        WebElement newPassword = findElementByContainingId(webDriver, "input[type=password]", "user_new_password");
        sendKeys(newPassword, updateAccountInfos.getPassword());

        WebElement confirmNewPassword = findElementByContainingId(webDriver, "input[type=password]", "user_confirm_new_password");
        sendKeys(confirmNewPassword, updateAccountInfos.getConfirmPassword());

        WebElement updatePasswordButton = findElementByText(webDriver, "button[type=submit]", "Update Password");
        updatePasswordButton.click();

        logout(webDriver);
        login(webDriver, enterpriseEndUser.getLoginId(), updateAccountInfos.getPassword());
    }

}
