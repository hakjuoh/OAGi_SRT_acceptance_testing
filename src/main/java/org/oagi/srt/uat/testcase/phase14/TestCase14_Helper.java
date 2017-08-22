package org.oagi.srt.uat.testcase.phase14;

import org.apache.commons.lang3.StringUtils;
import org.oagi.srt.uat.CreateCodeListInputs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

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

        WebElement agencyIdElement = findElementByContainingId(webDriver, "span.ui-autocomplete", "agencyIdListValueID");
        assertNotNull(agencyIdElement);
        clickDropdownElement(webDriver, agencyIdElement, codeListInputs.getAgencyId());

        sendKeysInputText(webDriver, "version", codeListInputs.getVersion());
        sendKeysInputText(webDriver, "definition", codeListInputs.getDefinition());
        sendKeysInputText(webDriver, "definitionSource", codeListInputs.getDefinitionSource());
        sendKeysInputText(webDriver, "remark", codeListInputs.getRemark());

        WebElement extensibleElement = webDriver.findElement(By.cssSelector("input[type=checkbox]"));
        assertNotNull(extensibleElement);
        if (codeListInputs.isExtensible()) {
            click(extensibleElement);
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

        click(findElementByText(webDriver, "button[type=submit]", "Save"));
    }

    public static WebElement searchCodeListByName(WebDriver webDriver, String codeListName) {
        gotoSubMenu(webDriver, "Code List", "View Code List");

        WebElement searchText = findElementByContainingId(webDriver, "input[type=text]", "ccName_input");
        sendKeys(searchText, codeListName);

        WebDriverWait wait = new WebDriverWait(webDriver, 2L);
        click(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.ui-autocomplete-item[data-item-value='" + codeListName + "']"))));
        click(findElementByText(webDriver, "button[type=submit]", "Search"));

        return findElementByText(webDriver, "tbody > tr[data-ri='0'] > td > a", codeListName);
    }

}
