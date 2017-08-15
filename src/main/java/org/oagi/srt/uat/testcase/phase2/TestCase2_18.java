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

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_18 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver_1, webDriver_2;

    @Autowired
    private Random random;

    @After
    public void tearDown() {
        webDriver_1.close();
        webDriver_2.close();
    }

    @Test
    public void testRevokeAdminDeveloperUserRoleToAnotherAdminDeveloper() {
        CreateAccountInputs admin_1 = createAdminDeveloper(webDriver_1, random);
        CreateAccountInputs admin_2 = createAdminDeveloper(webDriver_2, random);

        login(webDriver_1, admin_1.getLoginId(), admin_1.getPassword());
        login(webDriver_2, admin_2.getLoginId(), admin_2.getPassword());

        gotoManagePage(webDriver_1, admin_2);
        gotoManagePage(webDriver_2, admin_1);

        revokeRole(webDriver_1, UserType.OAGI, UserRole.AdminDeveloper);
        gotoManagePage(webDriver_1, admin_2);

        assertNull(findElementByText(webDriver_1, "td", UserType.OAGI.toString(), true));
        assertNull(findElementByText(webDriver_1, "td", UserRole.AdminDeveloper.toString(), true));

        revokeRole(webDriver_2, UserType.OAGI, UserRole.AdminDeveloper);

        String errorMessage = getErrorMessage(webDriver_2);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }
}
