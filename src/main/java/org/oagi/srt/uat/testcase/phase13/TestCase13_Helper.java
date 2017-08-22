package org.oagi.srt.uat.testcase.phase13;

import org.apache.commons.lang3.StringUtils;
import org.oagi.srt.uat.testcase.CreateContextSchemeInputs;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

public class TestCase13_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase13_Helper.class);

    public static String createContextCategory(WebDriver webDriver, Random random) {
        int randomNo = random.nextInt(10000000);
        String suffix = String.format("%08d", randomNo);
        String ctxCatName = "test_ctx_cat_" + suffix;

        createContextCategory(webDriver, ctxCatName, ctxCatName);

        return ctxCatName;
    }

    public static String createContextCategory(WebDriver webDriver, String ctxCatName, String description) {
        gotoSubMenu(webDriver, "Context Management", "Context Category");

        WebElement createButton = findElementByText(webDriver, "button[type=button]", "Create Context Category");
        createButton.click();

        WebElement nameElement = findElementByContainingId(webDriver, "input[type=text]", "name");
        sendKeys(nameElement, ctxCatName);

        WebElement descriptionElement = webDriver.findElement(By.cssSelector("textarea"));
        sendKeys(descriptionElement, description);

        WebElement create = webDriver.findElement(By.cssSelector("input[type=submit]"));
        create.click();

        return ctxCatName;
    }

    public static WebElement searchContextCategoryByName(WebDriver webDriver, String ctxCatName) {
        gotoSubMenu(webDriver, "Context Management", "Context Category");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "name_input");
        sendKeys(searchText, ctxCatName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + ctxCatName + "']")));
        element.click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", ctxCatName);
    }

    public static void deleteContextCategory(WebDriver webDriver, String ctxCatName) {
        gotoSubMenu(webDriver, "Context Management", "Context Category");
        WebElement row = searchContextCategoryByName(webDriver, ctxCatName);
        assertNotNull(row);
        row.click();

        WebElement discardButton = findElementByText(webDriver, "button[type=submit]", "Discard");
        discardButton.click();

        WebElement acceptButton = findElementByText(webDriver, "button[type=submit]", "Accept");
        acceptButton.click();
    }

    public static String editContextCategory(WebDriver webDriver, Random random, String ctxCatName) {
        int randomNo = random.nextInt(10000000);
        String suffix = String.format("%08d", randomNo);
        String updateCtxCatName = "test_ctx_cat_" + suffix;

        editContextCategory(webDriver, ctxCatName, updateCtxCatName, updateCtxCatName);

        return updateCtxCatName;
    }

    public static void editContextCategory(WebDriver webDriver, String ctxCatName, String newCtxCatName, String description) {
        WebElement row = searchContextCategoryByName(webDriver, ctxCatName);
        assertNotNull(row);

        WebElement parent = row.findElement(By.xpath("./../.."));
        String dataRi = parent.getAttribute("data-ri");

        WebElement link = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > a", ctxCatName);
        link.click();

        WebElement nameElement = findElementByContainingId(webDriver, "input[type=text]", "name");
        nameElement.clear();
        sendKeys(nameElement, newCtxCatName);

        WebElement descriptionElement = webDriver.findElement(By.cssSelector("textarea"));
        descriptionElement.clear();
        sendKeys(descriptionElement, description);

        WebElement update = findElementByText(webDriver, "button[type=submit]", "Update");
        update.click();
    }

    public static CreateContextSchemeInputs createContextScheme(WebDriver webDriver, Random random, String ctxCatName) {
        CreateContextSchemeInputs contextSchemeInputs = CreateContextSchemeInputs.generateRandomly(random, ctxCatName);
        createContextScheme(webDriver, contextSchemeInputs);

        return contextSchemeInputs;
    }

    public static void createContextScheme(WebDriver webDriver, CreateContextSchemeInputs contextSchemeInputs) {
        logger.info("Attempting to create context scheme using " + contextSchemeInputs);

        gotoSubMenu(webDriver, "Context Management", "Context Scheme");
        WebElement createButton = findElementByText(webDriver, "button[type=button]", "Create Context Scheme");
        createButton.click();

        String ctxCatName = contextSchemeInputs.getContextCategory();
        if (!StringUtils.isEmpty(ctxCatName)) {
            WebElement ctxCatElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "inputContextCategory");
            clickDropdownElement(webDriver, ctxCatElement, ctxCatName);
        }

        WebElement nameElement = findElementByContainingId(webDriver, "input[type=text]", "name");
        sendKeys(nameElement, contextSchemeInputs.getName());

        WebElement schemeIdElement = findElementByContainingId(webDriver, "input[type=text]", "schemeId");
        sendKeys(schemeIdElement, contextSchemeInputs.getSchemeId());

        WebElement agencyIdElement = findElementByContainingId(webDriver, "input[type=text]", "agencyId");
        sendKeys(agencyIdElement, contextSchemeInputs.getAgencyId());

        WebElement versionElement = findElementByContainingId(webDriver, "input[type=text]", "version");
        sendKeys(versionElement, contextSchemeInputs.getVersion());

        WebElement descriptionElement = webDriver.findElement(By.cssSelector("textarea"));
        sendKeys(descriptionElement, contextSchemeInputs.getDescription());

        String value = contextSchemeInputs.getValue();
        String meaning = contextSchemeInputs.getMeaning();
        if (!StringUtils.isEmpty(value) || !StringUtils.isEmpty(meaning)) {
            WebElement addButton = findElementByText(webDriver, "button[type=submit]", "Add");
            addButton.click();

            WebDriverWait wait = new WebDriverWait(webDriver, 5L);

            if (!StringUtils.isEmpty(value)) {
                WebElement valueElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Value']")));
                valueElement.findElement(By.xpath("./../../..")).click();
                valueElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Value'].ui-state-focus")));
                sendKeys(valueElement, value);
            }

            if (!StringUtils.isEmpty(meaning)) {
                WebElement meaningElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Meaning']")));
                meaningElement.findElement(By.xpath("./../../..")).click();
                meaningElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Meaning'].ui-state-focus")));
                sendKeys(meaningElement, contextSchemeInputs.getMeaning());
            }

            // WebElement deleteButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td button[type=submit]")));
        }

        WebElement create = webDriver.findElement(By.cssSelector("input[type=submit]"));
        create.click();
    }

    public static WebElement searchContextSchemeByName(WebDriver webDriver, String ctxSchName) {
        gotoSubMenu(webDriver, "Context Management", "Context Scheme");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "name_input");
        sendKeys(searchText, ctxSchName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + ctxSchName + "']")));
        element.click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS)) {
            try {
                webDriver.findElement(By.cssSelector("tbody > tr[data-ri='1'] > td"));
            } catch (NoSuchElementException e) {
                break;
            }
        }

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", ctxSchName);
    }

    public static WebElement searchSharedContextSchemeByName(WebDriver webDriver, String ctxSchName) {
        gotoSubMenu(webDriver, "Context Management", "Context Scheme");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "name_input");
        sendKeys(searchText, ctxSchName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + ctxSchName + "']")));
        element.click();

        WebElement searchButton = findElementByText(webDriver, "button[type=submit]", "Search");
        searchButton.click();

        long s = System.currentTimeMillis();
        while (!isTimeout(s, 2L, TimeUnit.SECONDS)) {
            try {
                webDriver.findElement(By.cssSelector("tbody > tr[data-ri='1'] > td"));
            } catch (NoSuchElementException e) {
                break;
            }
        }

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td", ctxSchName);
    }

    public static CreateContextSchemeInputs editContextScheme(WebDriver webDriver, Random random, CreateContextSchemeInputs contextSchemeInputs) {
        CreateContextSchemeInputs updatedContextSchemeInputs = CreateContextSchemeInputs.generateRandomly(random, contextSchemeInputs.getContextCategory());
        editContextScheme(webDriver, contextSchemeInputs.getName(), updatedContextSchemeInputs);
        return updatedContextSchemeInputs;
    }

    public static void editContextScheme(WebDriver webDriver, CreateContextSchemeInputs contextSchemeInputs) {
        editContextScheme(webDriver, contextSchemeInputs.getName(), contextSchemeInputs);
    }

    public static void editContextScheme(WebDriver webDriver, String searchName, CreateContextSchemeInputs contextSchemeInputs) {
        gotoSubMenu(webDriver, "Context Management", "Context Scheme");

        while (true) {
            try {
                WebElement link = searchContextSchemeByName(webDriver, searchName);
                link.click();
                break;
            } catch (StaleElementReferenceException e) {
            }
        }


        WebElement nameElement = findElementByContainingId(webDriver, "input[type=text]", "name");
        nameElement.clear();
        sendKeys(nameElement, contextSchemeInputs.getName());

        WebElement schemeIdElement = findElementByContainingId(webDriver, "input[type=text]", "schemeId");
        schemeIdElement.clear();
        sendKeys(schemeIdElement, contextSchemeInputs.getSchemeId());

        WebElement agencyIdElement = findElementByContainingId(webDriver, "input[type=text]", "agencyId");
        agencyIdElement.clear();
        sendKeys(agencyIdElement, contextSchemeInputs.getAgencyId());

        WebElement versionElement = findElementByContainingId(webDriver, "input[type=text]", "version");
        versionElement.clear();
        sendKeys(versionElement, contextSchemeInputs.getVersion());

        WebElement descriptionElement = webDriver.findElement(By.cssSelector("textarea"));
        descriptionElement.clear();
        sendKeys(descriptionElement, contextSchemeInputs.getDescription());

        try {
            webDriver.findElement(By.cssSelector("tbody > tr[data-ri='0']"));

            WebDriverWait wait = new WebDriverWait(webDriver, 5L);

            WebElement valueElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Value']")));
            valueElement.findElement(By.xpath("./../../..")).click();
            valueElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Value'].ui-state-focus")));
            valueElement.clear();
            sendKeys(valueElement, contextSchemeInputs.getValue());

            WebElement meaningElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Meaning']")));
            meaningElement.findElement(By.xpath("./../../..")).click();
            meaningElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody > tr[data-ri='0'] > td input[data-p-label='Meaning'].ui-state-focus")));
            meaningElement.clear();
            sendKeys(meaningElement, contextSchemeInputs.getMeaning());

        } catch (NoSuchElementException e) {
        }

        WebElement update = findElementByText(webDriver, "button[type=submit]", "Update");
        update.click();
    }
}
