package org.oagi.srt.uat.testcase.phase3;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
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
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_1.gotoPageForEnterprise;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase3_2 {

    private static Logger logger = LoggerFactory.getLogger(TestCase3_1.class);

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs admin = CreateAccountInputs.OAGI_ADMIN;

    private CreateEnterpriseInputs enterprise;

    @Before
    public void setUp() {
        // admin = createAdminDeveloper(webDriver, random);

        enterprise = createEnterprise(webDriver, random, admin);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testDeactivateEnterprise() {
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.AdminUser);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(webDriver, enterprise.getEnterpriseName(), "Edit");

        WebElement deactivateButton = findElementByText(webDriver, "button[type=submit]", "Deactivate");
        deactivateButton.click();

        WebElement acceptButton = findElementByText(webDriver, "button[type=submit]", "Accept");
        acceptButton.click();


        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(webDriver, enterprise.getEnterpriseName(), "Edit");

        WebElement activateButton = findElementByText(webDriver, "button[type=submit]", "Activate");
        assertNotNull(activateButton);
    }

    @Test
    public void testEnterpriseAdminUserCannotLoginAfterEnterpriseIsDeactivated() {
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.AdminUser);

        testEnterpriseUserCannotLoginAfterEnterpriseIsDeactivated(accountInputs);

        String errorMessage = getErrorMessageOnLoginPage();
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    @Test
    public void testEnterpriseEndUserCannotLoginAfterEnterpriseIsDeactivated() {
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.EndUser);

        testEnterpriseUserCannotLoginAfterEnterpriseIsDeactivated(accountInputs);

        String errorMessage = getErrorMessageOnLoginPage();
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    private void testEnterpriseUserCannotLoginAfterEnterpriseIsDeactivated(CreateAccountInputs enterpriseUser) {
        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(webDriver, enterprise.getEnterpriseName(), "Edit");

        WebElement deactivateButton = findElementByText(webDriver, "button[type=submit]", "Deactivate");
        deactivateButton.click();

        WebElement acceptButton = findElementByText(webDriver, "button[type=submit]", "Accept");
        acceptButton.click();

        logout(webDriver);
        login(webDriver, enterpriseUser);
    }

    private String getErrorMessageOnLoginPage() {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert-danger")));
        assertNotNull(errorMessageElement);

        return errorMessageElement.getText();
    }

    @Test
    public void testEnterpriseAdminUserCanLoginAfterEnterpriseIsReactivated() {
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.AdminUser);

        testEnterpriseUserCanLoginAfterEnterpriseIsReactivated(accountInputs);
    }

    @Test
    public void testEnterpriseEndUserCanLoginAfterEnterpriseIsReactivated() {
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.EndUser);

        testEnterpriseUserCanLoginAfterEnterpriseIsReactivated(accountInputs);
    }

    private void testEnterpriseUserCanLoginAfterEnterpriseIsReactivated(CreateAccountInputs enterpriseUser) {
        // Deactivate
        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(webDriver, enterprise.getEnterpriseName(), "Edit");

        WebElement deactivateButton = findElementByText(webDriver, "button[type=submit]", "Deactivate");
        deactivateButton.click();

        WebElement acceptButton = findElementByText(webDriver, "button[type=submit]", "Accept");
        acceptButton.click();

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(webDriver, enterprise.getEnterpriseName(), "Edit");

        // Reactivate
        WebElement activateButton = findElementByText(webDriver, "button[type=submit]", "Activate");
        activateButton.click();

        logout(webDriver);
        login(webDriver, enterpriseUser, true);
    }
}
