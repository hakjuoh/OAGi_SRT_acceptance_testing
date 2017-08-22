package org.oagi.srt.uat.testcase.phase14;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_4_12 {

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
    public void testEnterpriseAdminUserCanShareContextSchemeCreatedByAnotherAdminUser() {
        CreateAccountInputs enterpriseAdmin_1 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_1, enterprise, UserRole.AdminUser);

        CreateAccountInputs enterpriseAdmin_2 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_2, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);

        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin_2);


        assertShareContextScheme(contextSchemeInputs);
    }

    @Test(expected = TimeoutException.class)
    public void testEnterpriseAdminUserCanShareContextSchemeCreatedByEndUser() {
        CreateAccountInputs enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        CreateAccountInputs enterpriseEndUser = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, enterpriseEndUser);

        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin);


        assertShareContextScheme(contextSchemeInputs);
    }

    @Test(expected = TimeoutException.class)
    public void testEnterpriseAdminUserCanShareContextSchemeCreatedByHimself() {
        CreateAccountInputs enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs.getName()));

        assertShareContextScheme(contextSchemeInputs);
    }

    private void assertShareContextScheme(CreateContextSchemeInputs contextSchemeInputs) {
        String dataRi;
        while (true) {
            try {
                WebElement row = searchContextSchemeByName(webDriver, contextSchemeInputs.getName());
                WebElement parent = row.findElement(By.xpath("./../.."));
                dataRi = parent.getAttribute("data-ri");
                break;
            } catch (StaleElementReferenceException e) {
            }
        }

        assertEquals("0", dataRi);
        WebElement shareButton = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share");
        assertNotNull(shareButton);
        shareButton.click();

        // ensure that the context category has been shared
        searchContextSchemeByName(webDriver, contextSchemeInputs.getName());

        findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share", true);
    }

}
