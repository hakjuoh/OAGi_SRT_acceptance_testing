package org.oagi.srt.uat.testcase.phase13;

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

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.createContextCategory;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.searchContextCategoryByName;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase13_1_3 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin;
    private CreateAccountInputs enterpriseEndUser_1;
    private CreateAccountInputs enterpriseEndUser_2;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        enterpriseEndUser_1 = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser_1.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser_1, UserRole.EndUser);

        enterpriseEndUser_2 = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser_2.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser_2, UserRole.EndUser);

        logout(webDriver);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testEndUserCanSeeCreatedContextCategoryByAnotherEndUser() {
        login(webDriver, enterpriseEndUser_1);

        String ctxCatName = createContextCategory(webDriver, random);
        WebElement row = searchContextCategoryByName(webDriver, ctxCatName);
        assertNotNull(row);

        logout(webDriver);
        login(webDriver, enterpriseEndUser_2);

        row = searchContextCategoryByName(webDriver, ctxCatName);
        assertNotNull(row);
    }

}
