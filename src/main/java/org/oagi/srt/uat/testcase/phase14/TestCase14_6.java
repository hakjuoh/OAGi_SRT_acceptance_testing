package org.oagi.srt.uat.testcase.phase14;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_6 {

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
    public void testEnterpriseAdminUserCannotEditContextSchemeWithoutSchemeId() {
        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        contextSchemeInputs.setSchemeId(null);
        contextSchemeInputs.setValue(null);
        contextSchemeInputs.setMeaning(null);

        editContextScheme(webDriver, contextSchemeInputs);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    @Test
    public void testEnterpriseAdminUserCannotEditContextSchemeWithoutAgencyId() {
        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        contextSchemeInputs.setAgencyId(null);
        contextSchemeInputs.setValue(null);
        contextSchemeInputs.setMeaning(null);

        editContextScheme(webDriver, contextSchemeInputs);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

    @Test
    public void testEnterpriseAdminUserCannotEditContextSchemeWithoutVersion() {
        String ctxCatName = createContextCategory(webDriver, random);

        CreateContextSchemeInputs contextSchemeInputs = createContextScheme(webDriver, random, ctxCatName);
        contextSchemeInputs.setVersion(null);
        contextSchemeInputs.setValue(null);
        contextSchemeInputs.setMeaning(null);

        editContextScheme(webDriver, contextSchemeInputs);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }

}
