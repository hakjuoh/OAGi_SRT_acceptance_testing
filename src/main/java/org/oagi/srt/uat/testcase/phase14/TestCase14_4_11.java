package org.oagi.srt.uat.testcase.phase14;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.login;
import static org.oagi.srt.uat.testcase.TestCaseHelper.logout;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_4_11 {

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

    @Test
    public void testEnterpriseAdminUserCanEditContextSchemeCreatedByAnotherAdminUser() {
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

        CreateContextSchemeInputs updateContextSchemeInputs = CreateContextSchemeInputs.generateRandomly(random, ctxCatName);
        editContextScheme(webDriver, contextSchemeInputs_1.getName(), updateContextSchemeInputs);

        assertNotNull(searchContextSchemeByName(webDriver, updateContextSchemeInputs.getName()));
    }

}
