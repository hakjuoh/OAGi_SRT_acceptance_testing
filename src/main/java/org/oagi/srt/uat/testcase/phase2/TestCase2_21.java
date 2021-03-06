package org.oagi.srt.uat.testcase.phase2;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.UserType;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.getErrorMessage;
import static org.oagi.srt.uat.testcase.TestCaseHelper.loginAsAdmin;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_21 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdminCannotAssignRootRole() {
        CreateAccountInputs admin = createAdminDeveloper(webDriver, random);
        loginAsAdmin(webDriver);

        gotoManagePage(webDriver, admin);
        addRole(webDriver, UserType.Root, UserRole.Root);
    }

    @Test
    public void testAdminCannotRevokeRootRole() {
        CreateAccountInputs root = createRoot(webDriver, random);
        loginAsAdmin(webDriver);

        gotoManagePage(webDriver, root);
        revokeRole(webDriver, UserType.Root, UserRole.Root);

        String errorMessage = getErrorMessage(webDriver);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }
}
