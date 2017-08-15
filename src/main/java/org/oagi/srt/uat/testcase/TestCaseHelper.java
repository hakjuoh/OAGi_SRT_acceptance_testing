package org.oagi.srt.uat.testcase;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestCaseHelper {

    private static final String BASE_TARGET_URL = "http://uatsftp.justransform.com/";

    public static void index(WebDriver webDriver) {
        webDriver.get(BASE_TARGET_URL);
    }

    public static void loginAsAdmin(WebDriver webDriver) {
        login(webDriver, "oagis", "oagis");
    }

    public static void login(WebDriver webDriver, String username, String password) {
        index(webDriver);

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
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
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
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        for (WebElement webElement : elements) {
            String id = webElement.getAttribute("id");
            if (id.contains(containingId)) {
                return webElement;
            }
        }
        return null;
    }

    public static List<WebElement> findDropdownElements(WebDriver webDriver, String containsId) {
        List<WebElement> autocompletePanels = webDriver.findElements(By.cssSelector("div.ui-autocomplete-panel"));
        for (WebElement autocompletePanel : autocompletePanels) {
            String id = autocompletePanel.getAttribute("id");
            if (id.contains(containsId)) {
                return webDriver.findElements(By.cssSelector("div[id='" + id + "'] > ul.ui-autocomplete-items > li"));
            }
        }
        throw new IllegalStateException();
    }

    public static void clickDropdownElement(WebDriver webDriver, WebElement element, String targetLabel) {
        WebElement dropdownButton = webDriver.findElement(By.cssSelector("span[id='" + element.getAttribute("id") + "'] > button.ui-autocomplete-dropdown"));
        dropdownButton.click();

        String elementId = element.getAttribute("id");
        String containsId = elementId.substring(elementId.indexOf(':') + 1, elementId.length());

        while (StringUtils.isEmpty(getInputTextElement(webDriver, elementId).getAttribute("value"))) {
            List<WebElement> dropdownElements = findDropdownElements(webDriver, containsId);
            for (WebElement dropdownElement : dropdownElements) {
                String itemLabel = dropdownElement.getAttribute("data-item-label");
                if (targetLabel.equals(itemLabel)) {
                    while (StringUtils.isEmpty(getInputTextElement(webDriver, elementId).getAttribute("value"))) {
                        try {
                            dropdownElement.click();
                        } catch (ElementNotVisibleException ignore) {
                        }
                    }
                }
            }
        }
    }

    public static WebElement getInputTextElement(WebDriver webDriver, String id) {
        return webDriver.findElement(By.cssSelector("span[id='" + id + "'] > input[type='text']"));
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

        createAccountElements.setUserTypeElement(findElementByContainingId(webDriver, "span.ui-autocomplete", "type"));
        createAccountElements.setUserRoleElement(findElementByContainingId(webDriver, "span.ui-autocomplete", "role"));
        createAccountElements.setEnterpriseNameElement(findElementByContainingId(webDriver, "span.ui-autocomplete", "enterpriseName"));

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
