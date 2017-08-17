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

import static junit.framework.TestCase.assertEquals;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase6_1 {

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
    public void testEnterpriseEndUserCannotChangeUsername() {
        gotoSubMenu(webDriver, "Admin", "Personal information");

        WebElement loginId = findElementByContainingId(webDriver, "input[type=text]", "loginId");
        String readonlyAttr = loginId.getAttribute("readonly");
        assertEquals("readonly", readonlyAttr);
    }

    @Test
    public void testEnterpriseEndUserCanChangeName() {
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
    public void testEnterpriseEndUserCanChangePhone() {
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
    public void testEnterpriseEndUserCanChangeAddress() {
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
    public void testEnterpriseEndUserCanChangePersonalInformation() {
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
