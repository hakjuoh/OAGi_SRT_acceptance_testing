package org.oagi.srt.uat.testcase.phase2;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountElements;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.createAccountElementsOnIndexPage;
import static org.oagi.srt.uat.testcase.TestCaseHelper.login;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_0 {

    private static final String BASE_TARGET_URL = "http://uatsftp.justransform.com/";

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
    public void testCreateAccount() {
        webDriver.get(BASE_TARGET_URL);

        WebElement createAccountLinkElement = webDriver.findElement(By.cssSelector("div.create-account-callout a"));
        createAccountLinkElement.click();

        CreateAccountElements createAccountElements = createAccountElementsOnIndexPage(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        logger.info("Attempting to create account using " + createAccountInputs);

        createAccountElements.getLoginIdElement().sendKeys(createAccountInputs.getLoginId());
        createAccountElements.getNameElement().sendKeys(createAccountInputs.getName());
        createAccountElements.getAddressElement().sendKeys(createAccountInputs.getAddress());
        createAccountElements.getMobileNoElement().clear();
        createAccountElements.getMobileNoElement().sendKeys(createAccountInputs.getMobileNo());
        createAccountElements.getEmailAddressElement().sendKeys(createAccountInputs.getEmailAddress());
        createAccountElements.getPasswordElement().sendKeys(createAccountInputs.getPassword());
        createAccountElements.getConfirmPasswordElement().sendKeys(createAccountInputs.getConfirmPassword());

        WebElement createAccountBtnElement = webDriver.findElement(By.cssSelector("button[type=submit]"));
        createAccountBtnElement.click();

        login(webDriver, createAccountInputs.getLoginId(), createAccountInputs.getPassword());
    }
}
