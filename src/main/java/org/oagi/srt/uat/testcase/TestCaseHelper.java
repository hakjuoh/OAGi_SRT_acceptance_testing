package org.oagi.srt.uat.testcase;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestCaseHelper {

    private static final String BASE_TARGET_URL = "http://uatsftp.justransform.com/";

    public static void loginAsAdmin(WebDriver webDriver) {
        login(webDriver, "oagis", "oagis");
    }

    public static void login(WebDriver webDriver, String username, String password) {
        webDriver.get(BASE_TARGET_URL);

        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement signinBtnElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=submit].btn-block")));

        WebElement usernameElement = webDriver.findElement(By.id("username"));
        usernameElement.sendKeys(username);

        WebElement passwordElement = webDriver.findElement(By.id("password"));
        passwordElement.sendKeys(password);

        signinBtnElement.submit();

        WebElement profileMenuElement = webDriver.findElement(By.cssSelector("ul.navbar-right > li > a.dropdown-toggle"));
        String text = profileMenuElement.getText();

        assertEquals(username, text);
    }

    public static void logout(WebDriver webDriver) {
        webDriver.get(BASE_TARGET_URL + "logout");
    }

    public static void sendKeys(WebElement webElement, String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        webElement.sendKeys(key);
    }

    public static void gotoMenu(WebDriver webDriver, String menuName) {
        WebElement menu = findElementByText(webDriver, "ul.navbar-nav > li > a", menuName);
        menu.click();
    }

    public static void gotoSubMenu(WebDriver webDriver, String menuName, String submenuName) {
        gotoMenu(webDriver, menuName);

        WebElement submenu = findElementByText(webDriver, "ul.dropdown-menu > li > a", submenuName);
        submenu.click();
    }

    public static WebElement findElementByText(WebDriver webDriver, String cssSelector, String text) {
        List<WebElement> elements = webDriver.findElements(By.cssSelector(cssSelector));
        WebElement expectedElement = null;
        for (WebElement element : elements) {
            if (text.equals(element.getText())) {
                expectedElement = element;
                break;
            }
        }

        assertNotNull(expectedElement);
        return expectedElement;
    }

    public static WebElement findElementByContainingId(WebDriver webDriver, String cssSelector, String containingId) {
        List<WebElement> inputTextElements = webDriver.findElements(By.cssSelector(cssSelector));
        for (WebElement webElement : inputTextElements) {
            String id = webElement.getAttribute("id");
            if (id.contains(containingId)) {
                return webElement;
            }
        }
        return null;
    }

    public static CreateAccountElements createAccountElementsOnIndexPage(WebDriver webDriver) {
        CreateAccountElements createAccountElements = new CreateAccountElements(webDriver);

        createAccountElements.setLoginIdElement(findElementByContainingId(webDriver, "input[type=text]", "loginId"));
        createAccountElements.setNameElement(findElementByContainingId(webDriver, "input[type=text]", "username"));
        createAccountElements.setAddressElement(findElementByContainingId(webDriver, "input[type=text]", "address"));
        createAccountElements.setMobileNoElement(findElementByContainingId(webDriver, "input[type=text]", "mobileNo"));
        createAccountElements.setEmailAddressElement(findElementByContainingId(webDriver, "input[type=text]", "emial"));
        createAccountElements.setPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_password"));
        createAccountElements.setConfirmPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_confirm_password"));

        assertNotNull(createAccountElements.getLoginIdElement());
        assertNotNull(createAccountElements.getNameElement());

        assertNotNull(createAccountElements.getAddressElement());
        assertNotNull(createAccountElements.getMobileNoElement());
        assertNotNull(createAccountElements.getEmailAddressElement());

        assertNotNull(createAccountElements.getPasswordElement());
        assertNotNull(createAccountElements.getConfirmPasswordElement());

        return createAccountElements;
    }

    public static CreateAccountElements createAccountElementsOnAdminPage(WebDriver webDriver) {
        CreateAccountElements createAccountElements = new CreateAccountElements(webDriver);

        createAccountElements.setLoginIdElement(findElementByContainingId(webDriver, "input[type=text]", "loginId"));
        createAccountElements.setNameElement(findElementByContainingId(webDriver, "input[type=text]", "username"));
        createAccountElements.setAddressElement(findElementByContainingId(webDriver, "input[type=text]", "address"));
        createAccountElements.setMobileNoElement(findElementByContainingId(webDriver, "input[type=text]", "mobileNo"));
        createAccountElements.setEmailAddressElement(findElementByContainingId(webDriver, "input[type=text]", "email"));

        List<WebElement> autoCompleteElements = webDriver.findElements(By.cssSelector("tr > td > span.ui-autocomplete"));
        for (WebElement autoCompleteElement : autoCompleteElements) {
            String id = autoCompleteElement.getAttribute("id");
            WebElement dropdownButton = webDriver.findElement(By.cssSelector("span[id='" + id + "'] > button.ui-autocomplete-dropdown"));
            if (id.contains("type")) {
                createAccountElements.setUserTypeElement(autoCompleteElement, dropdownButton);
            } else if (id.contains("role")) {
                createAccountElements.setUserRoleElement(autoCompleteElement, dropdownButton);
            } else if (id.contains("enterpriseName")) {
                createAccountElements.setEnterpriseNameElement(autoCompleteElement, dropdownButton);
            }
        }

        createAccountElements.setPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_password"));
        createAccountElements.setConfirmPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_confirm_password"));

        assertNotNull(createAccountElements.getLoginIdElement());
        assertNotNull(createAccountElements.getNameElement());

        assertNotNull(createAccountElements.getUserTypeElement());
        assertNotNull(createAccountElements.getUserRoleElement());
        assertNotNull(createAccountElements.getEnterpriseNameElement());

        assertNotNull(createAccountElements.getAddressElement());
        assertNotNull(createAccountElements.getMobileNoElement());
        assertNotNull(createAccountElements.getEmailAddressElement());

        assertNotNull(createAccountElements.getPasswordElement());
        assertNotNull(createAccountElements.getConfirmPasswordElement());

        return createAccountElements;
    }

    public static String getErrorMessage(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.ui-messages-error-detail")));
        assertNotNull(errorMessageElement);

        return errorMessageElement.getText();
    }
}
