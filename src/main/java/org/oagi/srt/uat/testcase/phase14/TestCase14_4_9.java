package org.oagi.srt.uat.testcase.phase14;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_4_9 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test(expected = TimeoutException.class)
    public void testEnterpriseAdminUserCanDeleteContextSchemeCreatedByAnotherAdminUser() {
        CreateAccountInputs enterpriseAdmin_1 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_1, enterprise, UserRole.AdminUser);

        CreateAccountInputs enterpriseAdmin_2 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_2, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);

        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs_1 = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin_2);


        discardContextScheme(contextSchemeInputs_1);
        searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName());
    }

    @Test(expected = TimeoutException.class)
    public void testEnterpriseAdminUserCanDeleteContextSchemeCreatedByEndUser() {
        CreateAccountInputs enterpriseAdmin_1 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_1, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);

        CreateAccountInputs enterpriseEndUser = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, enterpriseEndUser);

        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs_1 = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);


        discardContextScheme(contextSchemeInputs_1);
        searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName());
    }

    private void discardContextScheme(CreateContextSchemeInputs contextSchemeInputs) {
        while (true) {
            try {
                WebElement link = searchContextSchemeByName(webDriver, contextSchemeInputs.getName());
                link.click();
                break;
            } catch (StaleElementReferenceException e) {}
        }

        WebElement discardButton = findElementByText(webDriver, "button[type=submit]", "Discard");
        discardButton.click();

        WebElement acceptButton = findElementByText(webDriver, "button[type=submit]", "Accept");
        acceptButton.click();
    }

}
