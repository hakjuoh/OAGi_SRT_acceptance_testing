package org.oagi.srt.uat.testcase.phase1;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.oagi.srt.uat.testcase.TestCaseHelper.findElementByText;
import static org.oagi.srt.uat.testcase.TestCaseHelper.loginAsAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase1_1 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testLoginUsingAdmin() {
        loginAsAdmin(webDriver);

        findElementByText(webDriver, "ul.navbar-nav > li > a", "Admin");
    }

}
