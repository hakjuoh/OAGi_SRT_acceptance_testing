package org.oagi.srt.uat.testcase.phase2;

import org.junit.After;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase2_13 {

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
    public void testAssigningDeveloperUserRole() {
        CreateAccountInputs createAccountInputs = createFreeAccountAndAddRole(webDriver, random, UserType.OAGI, UserRole.Developer);
        login(webDriver, createAccountInputs);
        gotoManagePage(webDriver, createAccountInputs);

        assertNotNull(findElementByText(webDriver, "td", UserType.OAGI.toString()));
        assertNotNull(findElementByText(webDriver, "td", UserRole.Developer.toString()));
    }

    static CreateAccountInputs createRoot(WebDriver webDriver, Random random) {
        return createAccount(webDriver, random, UserType.Root, UserRole.Root);
    }

    static CreateAccountInputs createDeveloper(WebDriver webDriver, Random random) {
        return createAccount(webDriver, random, UserType.OAGI, UserRole.Developer);
    }

    static CreateAccountInputs createAdminDeveloper(WebDriver webDriver, Random random) {
        return createAccount(webDriver, random, UserType.OAGI, UserRole.AdminDeveloper);
    }

    static CreateAccountInputs createAccount(WebDriver webDriver, Random random, UserType userType, UserRole userRole) {
        loginAsAdmin(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setAddress(null);
        TestCase2_Helper.createAccount(webDriver, createAccountInputs, userType, userRole);
        logout(webDriver);

        return createAccountInputs;
    }

    static CreateAccountInputs createFreeAccountAndAddRole(WebDriver webDriver, Random random, UserType userType, UserRole userRole) {
        loginAsAdmin(webDriver);

        CreateAccountInputs createAccountInputs = CreateAccountInputs.generateRandomly(random);
        createAccountInputs.setAddress(null);
        TestCase2_Helper.createFreeAccount(webDriver, createAccountInputs);

        gotoManagePage(webDriver, createAccountInputs);
        addRole(webDriver, userType, userRole);
        revokeRole(webDriver, UserType.Free, UserRole.Free);
        logout(webDriver);

        return createAccountInputs;
    }

    static void gotoManagePage(WebDriver webDriver, CreateAccountInputs createAccountInputs) {
        gotoManagePage(webDriver, createAccountInputs.getLoginId());
    }

    static void gotoManagePage(WebDriver webDriver, String loginId) {
        index(webDriver);
        gotoSubMenu(webDriver, "Admin", "Manage Right for All Users");

        WebElement searchInputText = findElementByContainingId(webDriver, "input[type=text]", "loginId_input");
        searchInputText.sendKeys(loginId);
        findElementByText(webDriver, "span.ui-autocomplete-query", loginId).click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        List<WebElement> results = Collections.emptyList();
        while (results.size() != 1) {
            results = webDriver.findElements(By.cssSelector("tbody.ui-datatable-data > tr"));
        }

        WebElement manageLink = findElementByText(webDriver, "tbody.ui-datatable-data > tr > td > a", "Manage");
        manageLink.click();
    }

    static void addRole(WebDriver webDriver, UserType userType, UserRole userRole) {
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

    static void revokeRole(WebDriver webDriver, UserType userType, UserRole userRole) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> tableRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr.ui-widget-content")));
        for (WebElement tableRow : tableRows) {
            String dataRi = tableRow.getAttribute("data-ri");
            List<WebElement> tableDatas = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("tr[data-ri='" + dataRi + "'] > td")));
            boolean isUserType = false, isUserRole = false;
            for (WebElement tableData : tableDatas) {
                if (userType.toString().equals(tableData.getText())) {
                    isUserType = true;
                } else if (userRole.toString().equals(tableData.getText())) {
                    isUserRole = true;
                }
            }

            if (isUserType && isUserRole) {
                WebElement revokeButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr[data-ri='" + dataRi + "'] > td > input[type='submit']")));
                revokeButton.click();
                break;
            }
        }
    }
}
