package org.oagi.srt.uat.testcase;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public static void login(WebDriver webDriver, CreateAccountInputs createAccountInputs) {
        login(webDriver, createAccountInputs, false);
    }

    public static void login(WebDriver webDriver, CreateAccountInputs createAccountInputs, boolean ensure) {
        login(webDriver, createAccountInputs.getLoginId(), createAccountInputs.getPassword(), ensure);
    }

    public static void login(WebDriver webDriver, String username, String password) {
        login(webDriver, username, password, false);
    }

    public static void login(WebDriver webDriver, String username, String password, boolean ensure) {
        index(webDriver);

        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement signinBtnElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type=submit].btn-block")));

        WebElement usernameElement = webDriver.findElement(By.id("username"));
        usernameElement.sendKeys(username);

        WebElement passwordElement = webDriver.findElement(By.id("password"));
        passwordElement.sendKeys(password);

        signinBtnElement.submit();

        if (ensure) {
            WebElement profileMenuElement = webDriver.findElement(By.cssSelector("ul.navbar-right > li > a.dropdown-toggle"));
            String text = profileMenuElement.getText();

            assertEquals(username, text);
        }
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
        index(webDriver);

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS)) {
            try {
                WebElement menu = findElementByText(webDriver, "ul.navbar-nav > li > a", menuName, true);
                if (menu != null) {
                    menu.click();
                    return;
                } else {
                    break;
                }
            } catch (StaleElementReferenceException ignore) {
                continue;
            }
        }

        throw new IllegalArgumentException("Can't find given menu: " + menuName);
    }

    public static void gotoSubMenu(WebDriver webDriver, String menuName, String submenuName) {
        gotoMenu(webDriver, menuName);

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS)) {
            try {
                WebElement submenu = findElementByText(webDriver, "ul.dropdown-menu > li > a", submenuName, true);
                if (submenu != null) {
                    submenu.click();
                    return;
                } else {
                    break;
                }
            } catch (StaleElementReferenceException ignore) {
                continue;
            }
        }

        throw new IllegalArgumentException("Can't find given submenu: " + submenuName);
    }

    public static WebElement findElementByText(WebDriver webDriver, String cssSelector, String text) {
        return findElementByText(webDriver, cssSelector, text, false);
    }

    public static WebElement findElementByText(WebDriver webDriver, String cssSelector, String text, boolean nullReturn) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        WebElement expectedElement = null;
        for (WebElement element : elements) {
            String elementText = element.getText();
            if (!StringUtils.isEmpty(elementText)) {
                elementText = elementText.trim();
            }

            if (text.equals(elementText)) {
                expectedElement = element;
                break;
            }
        }

        if (!nullReturn) {
            assertNotNull(expectedElement);
        }
        return expectedElement;
    }

    public static WebElement findElementByContainingId(WebDriver webDriver, String cssSelector, String containingId) {
        return findElementByContainingId(webDriver, cssSelector, containingId, false);
    }

    public static WebElement findElementByContainingId(WebDriver webDriver, String cssSelector, String containingId, boolean nullReturn) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        WebElement expectedElement = null;
        for (WebElement webElement : elements) {
            String id = webElement.getAttribute("id");
            if (id.contains(containingId)) {
                expectedElement = webElement;
                break;
            }
        }

        if (!nullReturn) {
            assertNotNull(expectedElement);
        }
        return expectedElement;
    }

    public static List<WebElement> findDropdownElements(WebDriver webDriver, String containsId) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        List<WebElement> autocompletePanels = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.ui-autocomplete-panel")));
        for (WebElement autocompletePanel : autocompletePanels) {
            String id = autocompletePanel.getAttribute("id");
            if (id.contains(containsId)) {
                return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div[id='" + id + "'] > ul.ui-autocomplete-items > li")));
            }
        }
        throw new IllegalStateException();
    }

    public static void clickDropdownElement(WebDriver webDriver, WebElement element, String targetLabel) {
        WebElement dropdownButton = webDriver.findElement(By.cssSelector("span[id='" + element.getAttribute("id") + "'] > button.ui-autocomplete-dropdown"));
        dropdownButton.click();

        String elementId = element.getAttribute("id");
        String containsId = elementId.substring(elementId.indexOf(':') + 1, elementId.length());

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS) || !isValueFilled(webDriver, elementId)) {
            List<WebElement> dropdownElements = findDropdownElements(webDriver, containsId);
            boolean clicked = false;
            for (WebElement dropdownElement : dropdownElements) {
                String itemLabel = dropdownElement.getAttribute("data-item-label");
                if (targetLabel.equals(itemLabel)) {
                    try {
                        dropdownElement.click();
                        clicked = true;
                    } catch (ElementNotVisibleException ignore) {
                    }
                }
            }
            if (!clicked) {
                throw new IllegalArgumentException("Can't find given label: " + targetLabel);
            }
        }

        if (isValueFilled(webDriver, elementId)) {
            return;
        }
        throw new IllegalStateException();
    }

    private static boolean isValueFilled(WebDriver webDriver, String elementId) {
        return !StringUtils.isEmpty(getInputTextElement(webDriver, elementId).getAttribute("value"));
    }

    private static boolean isTimeout(long startTime, long timeout, TimeUnit timeUnit) {
        return (System.currentTimeMillis() - startTime) > timeUnit.convert(timeout, TimeUnit.MILLISECONDS);
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

    public static CreateAccountElements createAccountElementsOnEnterpriseAdminPage(WebDriver webDriver) {
        CreateAccountElements createAccountElements = new CreateAccountElements(webDriver);

        createAccountElements.setLoginIdElement(findElementByContainingId(webDriver, "input[type=text]", "loginId"));
        createAccountElements.setNameElement(findElementByContainingId(webDriver, "input[type=text]", "username"));
        createAccountElements.setAddressElement(findElementByContainingId(webDriver, "input[type=text]", "address"));
        createAccountElements.setMobileNoElement(findElementByContainingId(webDriver, "input[type=text]", "mobileNo"));
        createAccountElements.setEmailAddressElement(findElementByContainingId(webDriver, "input[type=text]", "email"));

        createAccountElements.setUserRoleElement(findElementByContainingId(webDriver, "select", "currentRole"));

        createAccountElements.setPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_password"));
        createAccountElements.setConfirmPasswordElement(findElementByContainingId(webDriver, "input[type=password]", "user_confirm_password"));

        assertNotNull(createAccountElements.getLoginIdElement());
        assertNotNull(createAccountElements.getNameElement());

        assertNotNull(createAccountElements.getUserRoleElement());

        assertNotNull(createAccountElements.getAddressElement());
        assertNotNull(createAccountElements.getMobileNoElement());
        assertNotNull(createAccountElements.getEmailAddressElement());

        assertNotNull(createAccountElements.getPasswordElement());
        assertNotNull(createAccountElements.getConfirmPasswordElement());

        return createAccountElements;
    }

    public static CreateEnterpriseElements createEnterpriseElements(WebDriver webDriver) {
        CreateEnterpriseElements enterpriseElements = new CreateEnterpriseElements(webDriver);

        enterpriseElements.setEnterpriseNameElement(findElementByContainingId(webDriver, "input[type=text]", "enterpriseName"));
        enterpriseElements.setFirstNameElement(findElementByContainingId(webDriver, "input[type=text]", "firstName"));
        enterpriseElements.setLastNameElement(findElementByContainingId(webDriver, "input[type=text]", "lastName"));
        enterpriseElements.setPhoneElement(findElementByContainingId(webDriver, "input[type=text]", "phone"));
        enterpriseElements.setAddressElement(findElementByContainingId(webDriver, "input[type=text]", "address"));
        enterpriseElements.setEmailElement(findElementByContainingId(webDriver, "input[type=text]", "email"));
        enterpriseElements.setPurgeDurationInMonthsElement(new Select(findElementByContainingId(webDriver, "select", "purgeDuration")));
        enterpriseElements.setSignedAgreementElement(findElementByContainingId(webDriver, "div.ui-chkbox", "agreement"));

        assertNotNull(enterpriseElements.getEnterpriseNameElement());
        assertNotNull(enterpriseElements.getFirstNameElement());
        assertNotNull(enterpriseElements.getLastNameElement());
        assertNotNull(enterpriseElements.getPhoneElement());
        assertNotNull(enterpriseElements.getAddressElement());
        assertNotNull(enterpriseElements.getEmailElement());
        assertNotNull(enterpriseElements.getPurgeDurationInMonthsElement());
        assertNotNull(enterpriseElements.getSignedAgreementElement());

        return enterpriseElements;
    }

    public static String getErrorMessage(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.ui-messages-error-detail")));
        assertNotNull(errorMessageElement);

        return errorMessageElement.getText();
    }

    public static String getErrorMessageOnLoginPage(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5L);
        WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert-danger")));
        assertNotNull(errorMessageElement);

        return errorMessageElement.getText();
    }
}
