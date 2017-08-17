package org.oagi.srt.uat.testcase.phase5;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase5_14 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }
    
    @Test
    public void testRevokingEndUserRoleByEnterpriseAdmin() {
        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, createAccountInputs, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        gotoSubMenu(webDriver, "Admin", "Manage Tenant’s Users");

        changeEnterpriseUserRole(webDriver, createAccountInputs.getLoginId(), "Revoke");

        logout(webDriver);
        login(webDriver, createAccountInputs);

        String errorMessage = getErrorMessageOnLoginPage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    static void changeEnterpriseUserRole(WebDriver webDriver, String userName, String buttonName) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);

        int lastDataRi = -1;
        WebElement nextButton = null;
        while (true) {
            try {
                List<WebElement> tableRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[role='row']")));
                int currentDataRi = -1;
                for (WebElement tableRow : tableRows) {
                    int dataRi = Integer.parseInt(tableRow.getAttribute("data-ri"));
                    if (dataRi < lastDataRi) {
                        break;
                    }

                    List<WebElement> tableData = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[data-ri='" + dataRi + "'] > td")));
                    boolean found = false;
                    for (WebElement tableDatum : tableData) {
                        if (userName.equals(tableDatum.getText())) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tbody > tr.ui-widget-content[data-ri='" + dataRi + "'] > td > input[type=submit]")));
                        for (WebElement button : buttons) {
                            if (buttonName.equals(button.getAttribute("value"))) {
                                button.click();
                                return;
                            }
                        }
                    }

                    currentDataRi = lastDataRi = dataRi;
                }

                if (currentDataRi == lastDataRi) {
                    nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ui-paginator > a:not(.ui-state-disabled)[aria-label='Next Page']")));
                    if (nextButton != null) {
                        nextButton.click();
                    } else {
                        break;
                    }
                }
            } catch (StaleElementReferenceException e) {
                continue;
            }
        }

        throw new IllegalStateException();
    }
}
