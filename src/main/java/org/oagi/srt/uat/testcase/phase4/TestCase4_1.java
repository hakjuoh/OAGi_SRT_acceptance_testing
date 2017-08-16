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
public class TestCase4_1 {

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
    public void testOAGIDeveloperCannotChangeUsername() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        WebElement loginId = findElementByContainingId(webDriver, "input[type=text]", "loginId");
        String readonlyAttr = loginId.getAttribute("readonly");
        assertEquals("readonly", readonlyAttr);
    }

    @Test
    public void testOAGIDeveloperCanChangeName() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement name = findElementByContainingId(webDriver, "input[type=text]", "name");
        name.clear();
        sendKeys(name, updateAccountInfos.getName());

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();

        gotoSubMenu(webDriver, "Admin", "Personal information");
        name = findElementByContainingId(webDriver, "input[type=text]", "name");
        assertEquals(updateAccountInfos.getName(), name.getAttribute("value"));
    }

    @Test
    public void testOAGIDeveloperCanChangePhone() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        phone.clear();
        sendKeys(phone, updateAccountInfos.getMobileNo());

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();

        gotoSubMenu(webDriver, "Admin", "Personal information");
        phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        assertEquals(updateAccountInfos.getMobileNo(), phone.getAttribute("value"));
    }

    @Test
    public void testOAGIDeveloperCanChangeAddress() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement address = findElementByContainingId(webDriver, "input[type=text]", "address");
        address.clear();
        sendKeys(address, updateAccountInfos.getAddress());

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();

        gotoSubMenu(webDriver, "Admin", "Personal information");
        address = findElementByContainingId(webDriver, "input[type=text]", "address");
        assertEquals(updateAccountInfos.getAddress(), address.getAttribute("value"));
    }

    @Test
    public void testOAGIDeveloperCanChangePersonalInformation() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Personal information");

        CreateAccountInputs updateAccountInfos = CreateAccountInputs.generateRandomly(random);
        WebElement name = findElementByContainingId(webDriver, "input[type=text]", "name");
        name.clear();
        sendKeys(name, updateAccountInfos.getName());

        WebElement phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        phone.clear();
        sendKeys(phone, updateAccountInfos.getMobileNo());

        WebElement address = findElementByContainingId(webDriver, "input[type=text]", "address");
        address.clear();
        sendKeys(address, updateAccountInfos.getAddress());

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();

        gotoSubMenu(webDriver, "Admin", "Personal information");
        name = findElementByContainingId(webDriver, "input[type=text]", "name");
        assertEquals(updateAccountInfos.getName(), name.getAttribute("value"));

        phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        assertEquals(updateAccountInfos.getMobileNo(), phone.getAttribute("value"));

        address = findElementByContainingId(webDriver, "input[type=text]", "address");
        assertEquals(updateAccountInfos.getAddress(), address.getAttribute("value"));
    }

}
