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
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createDeveloper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase4_2 {

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
    public void testOAGIDeveloperCanChangeEmailAddress() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement email = findElementByContainingId(webDriver, "input[type=text]", "email");
        email.clear();
        sendKeys(email, updateAccountInfos.getEmailAddress());

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();

        gotoSubMenu(webDriver, "Admin", "Personal information");
        email = findElementByContainingId(webDriver, "input[type=text]", "email");
        assertEquals(updateAccountInfos.getEmailAddress(), email.getAttribute("value"));
    }


}
