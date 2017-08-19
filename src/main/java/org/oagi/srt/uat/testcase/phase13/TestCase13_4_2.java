package org.oagi.srt.uat.testcase.phase13;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase13_4_2 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testShareContextScheme() {
        String ctxCatName = createContextCategory(webDriver, random);
        String ctxSchName = createContextScheme(webDriver, random, ctxCatName).getName();

        WebElement row = searchContextSchemeByName(webDriver, ctxSchName);
        WebElement parent = row.findElement(By.xpath("./../.."));
        String dataRi = parent.getAttribute("data-ri");

        WebElement shareButton = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share");
        assertNotNull(shareButton);
        shareButton.click();

        // ensure that the context category has been shared
        searchContextCategoryByName(webDriver, ctxCatName);
        shareButton = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share", true);
        assertNull(shareButton);

        logout(webDriver);
        loginAsAdmin(webDriver);

        row = searchSharedContextSchemeByName(webDriver, ctxSchName);
        assertNotNull(row);
    }

}
