package org.oagi.srt.uat.testcase.phase4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_13.createDeveloper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase4_5 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs developer;

    @Before
    public void setUp() {
        developer = createDeveloper(webDriver, random);
        logout(webDriver);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAGIDeveloperCannotChangePasswordUsingShortPassword() {
        login(webDriver, developer);
        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");
    }


}
