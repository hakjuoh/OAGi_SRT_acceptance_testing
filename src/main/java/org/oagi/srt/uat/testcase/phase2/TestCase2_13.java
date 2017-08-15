package org.oagi.srt.uat.testcase.phase2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.oagi.srt.uat.testcase.UserType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createAccount;

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

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setAddress(null);
        createAccount(webDriver, createAccountInputs);

        gotoManagePage(createAccountInputs);
        addRole(UserType.OAGI, UserRole.Developer);

        gotoManagePage(createAccountInputs);

        assertNotNull(findElementByText(webDriver, "td", "OAGI Tenant"));
        assertNotNull(findElementByText(webDriver, "td", "Developer"));
    }

    private void gotoManagePage(CreateAccountInputs createAccountInputs) {
        index(webDriver);
        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        WebElement searchInputText = findElementByContainingId(webDriver, "input[type=text]", "loginId_input");
        searchInputText.sendKeys(createAccountInputs.getLoginId());
        findElementByText(webDriver, "span.ui-autocomplete-query", createAccountInputs.getLoginId()).click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        List<WebElement> results = Collections.emptyList();
        while (results.size() != 1) {
            results = webDriver.findElements(By.cssSelector("tbody.ui-datatable-data > tr"));
        }

        WebElement manageLink = findElementByText(webDriver, "tbody.ui-datatable-data > tr > td > a", "Manage");
        manageLink.click();
    }

    private void addRole(UserType userType, UserRole userRole) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement addRoleButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));
        addRoleButton.click();

        WebElement userTypeElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "type");
        String expectedUserType = userType.toString();
        clickDropdownElement(webDriver, userTypeElement, expectedUserType);

        WebElement userRoleElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "role");
        String expectedUserRole = userRole.toString();
        clickDropdownElement(webDriver, userRoleElement, expectedUserRole);

        WebElement assignRoleButton = findElementByText(webDriver, "button[type=submit]", "Assign a role");
        assignRoleButton.click();
    }
}
