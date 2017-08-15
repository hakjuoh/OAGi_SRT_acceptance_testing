package org.oagi.srt.uat.testcase.phase3;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.UserType;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.*;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createAdminDeveloper;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createDeveloper;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.gotoManagePage;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase3_1 {

    private static Logger logger = LoggerFactory.getLogger(TestCase3_1.class);

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs admin;

    @Before
    public void setUp() {
        this.admin = new CreateAccountInputs();
        this.admin.setLoginId("oagis");
        this.admin.setPassword("oagis");
        //createAdminDeveloper(webDriver, random);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testCreateEnterprise() {
        CreateEnterpriseInputs enterprise_1 = CreateEnterpriseInputs.generateRandomly(random);
        logger.info("Attempting to create enterprise using " + enterprise_1);
        createEnterprise(webDriver, random, admin, enterprise_1);
    }

    @Test
    public void testCreateEnterpriseWithoutEmailAddress() {
        CreateEnterpriseInputs enterprise_2 = CreateEnterpriseInputs.generateRandomly(random);
        enterprise_2.setEmailAddress(null);
        logger.info("Attempting to create enterprise using " + enterprise_2);
        createEnterprise(webDriver, random, admin, enterprise_2);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    @Test
    public void testCreateEnterpriseWithInvalidEmailAddress() {
        CreateEnterpriseInputs enterprise_3 = CreateEnterpriseInputs.generateRandomly(random);
        enterprise_3.setEmailAddress("invalid-email-address");
        logger.info("Attempting to create enterprise using " + enterprise_3);
        createEnterprise(webDriver, random, admin, enterprise_3);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    @Test
    public void testUpdateEnterprise() {
        CreateEnterpriseInputs enterprise = createEnterprise(webDriver, random, admin);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(enterprise.getEnterpriseName(), "Edit");

        CreateEnterpriseInputs updatesInfo = CreateEnterpriseInputs.generateRandomly(random);
        updateEnterprise(updatesInfo);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        gotoPageForEnterprise(updatesInfo.getEnterpriseName(), "View");

        WebElement enterpriseName = findElementByContainingId(webDriver, "input[type=text]", "enterpriseName");
        assertEquals(updatesInfo.getEnterpriseName(), enterpriseName.getAttribute("value"));

        WebElement firstName = findElementByContainingId(webDriver, "input[type=text]", "firstName");
        assertEquals(updatesInfo.getFirstName(), firstName.getAttribute("value"));

        WebElement lastName = findElementByContainingId(webDriver, "input[type=text]", "lastName");
        assertEquals(updatesInfo.getLastName(), lastName.getAttribute("value"));

        WebElement phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        assertEquals(updatesInfo.getPhone(), phone.getAttribute("value"));

        WebElement address = findElementByContainingId(webDriver, "input[type=text]", "address");
        assertEquals(updatesInfo.getAddress(), address.getAttribute("value"));

        WebElement email = findElementByContainingId(webDriver, "input[type=text]", "email");
        assertEquals(updatesInfo.getEmailAddress(), email.getAttribute("value"));
    }

    @Test
    public void testCreateEnterpriseAdminUser() {
        CreateEnterpriseInputs enterprise = createEnterprise(webDriver, random, admin);
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.AdminUser);

        gotoManagePage(webDriver, accountInputs);

        assertNotNull(findElementByText(webDriver, "td", UserType.Enterprise.toString() + "-" + enterprise.getEnterpriseName()));
        assertNotNull(findElementByText(webDriver, "td", UserRole.AdminUser.toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnterpriseAdminUserCannotCreateEnterprise() {
        CreateEnterpriseInputs enterprise = createEnterprise(webDriver, random, admin);
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, accountInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnterpriseEndUserCannotCreateEnterprise() {
        CreateEnterpriseInputs enterprise = createEnterprise(webDriver, random, admin);
        CreateAccountInputs accountInputs = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, accountInputs, enterprise, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, accountInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAGIDeveloperCannotCreateEnterprise() {
        CreateAccountInputs accountInputs = createDeveloper(webDriver, random);

        logout(webDriver);
        login(webDriver, accountInputs);

        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
    }

    private void gotoPageForEnterprise(String enterpriseName, String pageName) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);

        while (true) {
            try {
                List<WebElement> tableRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[role='row']")));
                for (WebElement tableRow : tableRows) {
                    String dataRi = tableRow.getAttribute("data-ri");
                    List<WebElement> tableData = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[data-ri='" + dataRi + "'] > td")));
                    boolean found = false;
                    for (WebElement tableDatum : tableData) {
                        if (enterpriseName.equals(tableDatum.getText())) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        List<WebElement> links = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[data-ri='" + dataRi + "'] > td > a")));
                        for (WebElement link : links) {
                            if (pageName.equals(link.getText())) {
                                link.click();
                                return;
                            }
                        }
                    }
                }

                WebElement nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ui-paginator > a:not(.ui-state-disabled)[aria-label='Next Page']")));
                if (nextButton != null) {
                    nextButton.click();
                } else {
                    break;
                }

            } catch (StaleElementReferenceException e) {
                continue;
            }
        }

        throw new IllegalStateException();
    }

    private void updateEnterprise(CreateEnterpriseInputs updatesInfo) {
        WebElement enterpriseName = findElementByContainingId(webDriver, "input[type=text]", "enterpriseName");
        WebElement firstName = findElementByContainingId(webDriver, "input[type=text]", "firstName");
        WebElement lastName = findElementByContainingId(webDriver, "input[type=text]", "lastName");
        WebElement phone = findElementByContainingId(webDriver, "input[type=text]", "phone");
        WebElement address = findElementByContainingId(webDriver, "input[type=text]", "address");
        WebElement email = findElementByContainingId(webDriver, "input[type=text]", "email");
        Select purgeDuration = new Select(findElementByContainingId(webDriver, "select", "purgeDuration"));

        enterpriseName.clear();
        sendKeys(enterpriseName, updatesInfo.getEnterpriseName());

        firstName.clear();
        sendKeys(firstName, updatesInfo.getFirstName());

        lastName.clear();
        sendKeys(lastName, updatesInfo.getLastName());

        phone.clear();
        sendKeys(phone, updatesInfo.getPhone());

        address.clear();
        sendKeys(address, updatesInfo.getAddress());

        email.clear();
        sendKeys(email, updatesInfo.getEmailAddress());

        purgeDuration.selectByValue(Integer.toString(updatesInfo.getPurgeDurationInMonths()));

        WebElement updateButton = findElementByText(webDriver, "button[type=submit]", "Update");
        updateButton.click();
    }

}
