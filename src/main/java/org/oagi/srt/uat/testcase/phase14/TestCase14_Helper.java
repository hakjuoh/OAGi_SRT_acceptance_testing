package org.oagi.srt.uat.testcase.phase14;

import org.apache.commons.lang3.StringUtils;
import org.oagi.srt.uat.CreateCodeListInputs;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.click;
import static org.oagi.srt.uat.testcase.phase13.TestCase13_Helper.fillEditableCell;

public class TestCase14_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase14_Helper.class);

    public static CreateCodeListInputs createCodeListWithoutBase(WebDriver webDriver, Random random) {
        CreateCodeListInputs codeListInputs = CreateCodeListInputs.generateRandomly(random);
        createCodeListWithoutBase(webDriver, codeListInputs);
        return codeListInputs;
    }

    public static void createCodeListWithoutBase(WebDriver webDriver, CreateCodeListInputs codeListInputs) {
        gotoSubMenu(webDriver, "Code List", "Create Code List w/o base");

        logger.info("Attempting to create code list using " + codeListInputs);

        sendKeysInputText(webDriver, "name", codeListInputs.getName());

        while (true) {
            try {
                WebElement agencyIdElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "agencyIdListValueID");
                assertNotNull(agencyIdElement);
                clickDropdownElement(webDriver, agencyIdElement, codeListInputs.getAgencyId());
                break;
            } catch (StaleElementReferenceException e) {
            }
        }

        sendKeysInputText(webDriver, "version", codeListInputs.getVersion());
        sendKeysInputText(webDriver, "definition", codeListInputs.getDefinition());
        sendKeysInputText(webDriver, "definitionSource", codeListInputs.getDefinitionSource());
        sendKeysInputText(webDriver, "remark", codeListInputs.getRemark());

        if (codeListInputs.isExtensible()) {
            WebElement extensibleElement;
            String id;
            while (true) {
                try {
                    extensibleElement = webDriver.findElement(By.cssSelector("input[type=checkbox]"));
                    assertNotNull(extensibleElement);
                    id = extensibleElement.getAttribute("id");
                    break;
                } catch (StaleElementReferenceException e) {
                }
            }

            while (true) {
                try {
                    extensibleElement = findElementByContainingId(webDriver, "input[type=checkbox]", id);
                    String checked = extensibleElement.getAttribute("checked");
                    if (!"true".equals(checked)) {
                        click(extensibleElement);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        break;
                    }
                } catch (StaleElementReferenceException e) {
                }
            }
        }

        String codeListValueCode = codeListInputs.getCodeListValueCode();
        String codeListValueShortName = codeListInputs.getCodeListValueShortName();
        String codeListValueDefinition = codeListInputs.getCodeListValueDefinition();
        String codeListValueDefinitionSource = codeListInputs.getCodeListValueDefinitionSource();

        if (!StringUtils.isEmpty(codeListValueCode) ||
                !StringUtils.isEmpty(codeListValueShortName) ||
                !StringUtils.isEmpty(codeListValueDefinition) ||
                !StringUtils.isEmpty(codeListValueDefinitionSource)) {

            click(findElementByText(webDriver, "button[type=submit]", "Add"));

            fillEditableCell(webDriver, "Code", codeListValueCode, 0);
            fillEditableCell(webDriver, "Short Name", codeListValueShortName, 0);
            fillEditableCell(webDriver, "Definition", codeListValueShortName, 0);
            fillEditableCell(webDriver, "Definition Source", codeListValueDefinitionSource, 0);
        }

        if (codeListInputs.isPublish()) {
            click(findElementByText(webDriver, "button[type=submit]", "Publish"));
        } else {
            click(findElementByText(webDriver, "button[type=submit]", "Save"));
        }
    }

    public static WebElement searchCodeListByName(WebDriver webDriver, String codeListName) {
        gotoSubMenu(webDriver, "Code List", "View Code List");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "ccName_input");
        sendKeys(searchText, codeListName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        click(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + codeListName + "']"))));
        click(findElementByText(webDriver, "button[type=submit]", "Search"));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", codeListName);
    }

    public static WebElement searchCodeListForCreateNewCodeListByName(WebDriver webDriver, String codeListName) {
        gotoSubMenu(webDriver, "Code List", "Create Code List from another");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "ccName_input");
        sendKeys(searchText, codeListName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        click(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + codeListName + "']"))));
        click(findElementByText(webDriver, "button[type=submit]", "Search"));

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", codeListName);
    }

    public static void share(WebDriver webDriver, WebElement element) {
        WebElement parent = element.findElement(By.xpath("./../.."));
        String dataRi = parent.getAttribute("data-ri");

        assertEquals("0", dataRi);
        WebElement shareButton = findElementByText(webDriver, "tr[data-ri='" + dataRi + "'] > td > button[type=submit]", "Share");
        assertNotNull(shareButton);
        click(shareButton);
    }

    public static void editCodeList(WebDriver webDriver, String codeListName, CreateCodeListInputs codeListInputs) {
        WebElement link = searchCodeListByName(webDriver, codeListName);
        click(link);

        logger.info("Attempting to edit code list using " + codeListInputs);

        sendKeysInputText(webDriver, "name", codeListInputs.getName());

        while (true) {
            try {
                WebElement agencyIdElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "agencyIdListValueID");
                assertNotNull(agencyIdElement);
                clickDropdownElement(webDriver, agencyIdElement, codeListInputs.getAgencyId());
                break;
            } catch (StaleElementReferenceException e) {
            }
        }

        sendKeysInputText(webDriver, "version", codeListInputs.getVersion());
        sendKeysInputText(webDriver, "definition", codeListInputs.getDefinition());
        sendKeysInputText(webDriver, "definitionSource", codeListInputs.getDefinitionSource());
        sendKeysInputText(webDriver, "remark", codeListInputs.getRemark());

        WebElement extensibleElement;
        String id;
        while (true) {
            try {
                extensibleElement = webDriver.findElement(By.cssSelector("input[type=checkbox]"));
                assertNotNull(extensibleElement);
                id = extensibleElement.getAttribute("id");
                break;
            } catch (StaleElementReferenceException e) {
            }
        }

        while (true) {
            try {
                extensibleElement = findElementByContainingId(webDriver, "input[type=checkbox]", id);
                String checked = extensibleElement.getAttribute("checked");
                if (  (codeListInputs.isExtensible() && !"true".equals(checked))  ||
                      (!codeListInputs.isExtensible() && checked != null)) {
                    click(extensibleElement);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                } else {
                    break;
                }
            } catch (StaleElementReferenceException e) {
            }
        }

        if (codeListInputs.isPublish()) {
            click(findElementByText(webDriver, "button[type=submit]", "Publish"));
        } else {
            click(findElementByText(webDriver, "button[type=submit]", "Update"));
        }
    }

}
