package org.oagi.srt.uat.testcase.phase2;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_1 {

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testCreateAccountWithMinimumInformation() {
        loginAsAdmin(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setAddress(null);
        createAccountInputs.setEmailAddress("hno2@nist.gov"); // to receive the verification email.
        TestCase2_Helper.createFreeAccount(webDriver, createAccountInputs);

        logout(webDriver);
        login(webDriver, createAccountInputs.getLoginId(), createAccountInputs.getPassword());
    }
}
