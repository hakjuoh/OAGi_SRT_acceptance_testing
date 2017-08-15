package org.oagi.srt.uat.testcase.phase2;

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

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.findElementByText;
import static org.oagi.srt.uat.testcase.TestCaseHelper.loginAsAdmin;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createFreeAccountAndAddRole;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.gotoManagePage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_15 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testAssigningAdminDeveloperUserRole() {
        CreateAccountInputs adminDeveloper = createFreeAccountAndAddRole(webDriver, random, UserType.OAGI, UserRole.AdminDeveloper);

        loginAsAdmin(webDriver);
        gotoManagePage(webDriver, adminDeveloper);

        assertNotNull(findElementByText(webDriver, "td", UserType.OAGI.toString()));
        assertNotNull(findElementByText(webDriver, "td", UserRole.AdminDeveloper.toString()));
    }
}
