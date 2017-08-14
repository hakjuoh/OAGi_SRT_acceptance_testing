package org.oagi.srt.uat.testcase.phase2;

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
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createAccount;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_2 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    @Test
    public void testCreateAccountWithAllInformation() throws InterruptedException {
        loginAsAdmin(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setEmailAddress("hno2@nist.gov"); // to receive the verification email.
        createAccount(webDriver, createAccountInputs);

        logout(webDriver);
        login(webDriver, createAccountInputs.getLoginId(), createAccountInputs.getPassword());
    }
}
