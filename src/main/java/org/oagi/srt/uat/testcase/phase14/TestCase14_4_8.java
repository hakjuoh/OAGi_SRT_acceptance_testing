package org.oagi.srt.uat.testcase.phase14;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
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
import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_4_8 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin_1, enterpriseAdmin_2;
    private CreateAccountInputs enterpriseEndUser;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin_1 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_1, enterprise, UserRole.AdminUser);

        enterpriseAdmin_2 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin_2, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);

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
    public void testEnterpriseAdminUserCanSeeContextSchemeDetailsCreatedByAnyone() {
        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs_1 = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin_1);

        CreateContextSchemeInputs contextSchemeInputs_2 = createContextScheme(webDriver, random, ctxCatName);
        assertNotNull(searchContextSchemeByName(webDriver, contextSchemeInputs_2.getName()));

        logout(webDriver);
        login(webDriver, enterpriseAdmin_2);


        WebElement link_1 = searchContextSchemeByName(webDriver, contextSchemeInputs_1.getName());
        link_1.click();

        WebElement name_1 = findElementByContainingId(webDriver, "input[type=text]", "name");
        assertEquals(contextSchemeInputs_1.getName(), name_1.getAttribute("value"));



        WebElement link_2 = searchContextSchemeByName(webDriver, contextSchemeInputs_2.getName());
        link_2.click();

        WebElement name_2 = findElementByContainingId(webDriver, "input[type=text]", "name");
        assertEquals(contextSchemeInputs_2.getName(), name_2.getAttribute("value"));
    }

}
