package org.oagi.srt.uat.testcase.phase2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.findElementByContainingId;
import static org.oagi.srt.uat.testcase.TestCaseHelper.gotoSubMenu;
import static org.oagi.srt.uat.testcase.TestCaseHelper.loginAsAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_13 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @Test
    public void testAssigningDeveloperUserRole() {
        loginAsAdmin(webDriver);
        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        findElementByContainingId(webDriver, "input[type=text]", "loginId_input");
    }
}
