package org.oagi.srt.uat.testcase.phase14;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.CreateCodeListInputs;
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.click;
import static org.oagi.srt.uat.testcase.phase14.TestCase14_Helper.createCodeListWithoutBase;
import static org.oagi.srt.uat.testcase.phase14.TestCase14_Helper.searchCodeListByName;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_Helper.createAccountByEnterpriseAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase14_11_2 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin;
    private CreateAccountInputs enterpriseEndUser;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver, enterpriseAdmin, enterprise, UserRole.AdminUser);

        logout(webDriver);
        login(webDriver, enterpriseAdmin);

        enterpriseEndUser = CreateAccountInputs.generateRandomly(random);
        enterpriseEndUser.setAddress(null);
        createAccountByEnterpriseAdmin(webDriver, enterpriseEndUser, UserRole.EndUser);

        logout(webDriver);
        login(webDriver, enterpriseEndUser);
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void testCreateCodeListBasedOnSharedCodeList() {
        CreateCodeListInputs codeListInputs = createCodeListWithoutBase(webDriver, random);
        WebElement row = searchCodeListByName(webDriver, codeListInputs.getName());
        share(row);

        searchCodeListByName(webDriver, codeListInputs.getName());
    }

    private void share(WebElement element) {
        WebElement parent = element.findElement(By.xpath("./../.."));
        String dataRi = parent.getAttribute("data-ri");

        assertEquals("0", dataRi);
        WebElement shareButton = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share");
        assertNotNull(shareButton);
        click(shareButton);
    }

}
