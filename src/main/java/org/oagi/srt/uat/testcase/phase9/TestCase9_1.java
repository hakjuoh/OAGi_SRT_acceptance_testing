package org.oagi.srt.uat.testcase.phase9;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.phase2.TestCase2_Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase9_1 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateAccountInputs freeUser;

    @Before
    public void setUp() {
        loginAsAdmin(webDriver);

        freeUser = CreateAccountInputs.generateRandomly(random);
        TestCase2_Helper.createFreeAccount(webDriver, freeUser);

        logout(webDriver);
        login(webDriver, freeUser.getLoginId(), freeUser.getPassword());
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAccessContextManagementMenu() {
        gotoSubMenu(webDriver, "Context Management", "Context Scheme");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAccessBusinessContextMenu() {
        gotoSubMenu(webDriver, "Context Management", "Business Context");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAccessCreateProfileBODMenu() {
        gotoSubMenu(webDriver, "Profile BODs", "Create Profile BOD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAccessCCManagementMenu() {
        gotoMenu(webDriver, "CC Management");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAccessProfileBODListMenu() {
        gotoSubMenu(webDriver, "Profile BODs", "Profile BOD List");
    }

    @Test
    public void testCanAccessProfileBODExpressionMenu() {
        gotoSubMenu(webDriver, "Profile BOD Expression", "Generate Expression");
    }

    @Test
    public void testGenerateProfileBOD() {
        gotoSubMenu(webDriver, "Profile BOD Expression", "Generate Expression");

        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.ui-chkbox-box")));

        assertTrue(elements.size() > 0);
        elements.get(0).click();

        WebElement generateButton = findElementByText(webDriver, "button[type=submit]", "Generate");
        boolean isOkay = false;
        long s = System.currentTimeMillis();
        while (!isTimeout(s, 5L, TimeUnit.SECONDS)) {
            String disabled;
            try {
                disabled = generateButton.getAttribute("disabled");
            } catch (StaleElementReferenceException e) {
                generateButton = findElementByText(webDriver, "button[type=submit]", "Generate");
                continue;
            }

            if ("true".equals(disabled) || "disabled".equals(disabled)) {
                continue;
            } else {
                isOkay = true;
                break;
            }
        }

        assertTrue(isOkay);
        generateButton.click();

        WebElement blockUI = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ui-blockui-content")));
        String style = blockUI.getAttribute("style");
        assertTrue(style.contains("display: block"));
    }

}
