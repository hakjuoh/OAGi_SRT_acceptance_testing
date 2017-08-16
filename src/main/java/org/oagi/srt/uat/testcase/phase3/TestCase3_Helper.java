package org.oagi.srt.uat.testcase.phase3;

import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseElements;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.oagi.srt.uat.testcase.TestCaseHelper.*;

public class TestCase3_Helper {

    private static Logger logger = LoggerFactory.getLogger(TestCase3_Helper.class);

    public static CreateEnterpriseInputs createEnterprise(WebDriver webDriver, Random random, CreateAccountInputs adminDeveloper) {
        CreateEnterpriseInputs createEnterpriseInputs = CreateEnterpriseInputs.generateRandomly(random);
        logger.info("Attempting to create enterprise using " + createEnterpriseInputs);

        createEnterprise(webDriver, adminDeveloper, createEnterpriseInputs);

        return createEnterpriseInputs;
    }

    public static void createEnterprise(WebDriver webDriver, CreateAccountInputs adminDeveloper, CreateEnterpriseInputs createEnterpriseInputs) {
        login(webDriver, adminDeveloper);
        gotoSubMenu(webDriver, "Admin", "Manage Enterprise");
        WebElement createEnterpriseBtn = findElementByText(webDriver, "button", "Create an enterprise");
        createEnterpriseBtn.click();

        CreateEnterpriseElements createEnterpriseElements = createEnterpriseElements(webDriver);

        sendKeys(createEnterpriseElements.getEnterpriseNameElement(), createEnterpriseInputs.getEnterpriseName());
        sendKeys(createEnterpriseElements.getFirstNameElement(), createEnterpriseInputs.getFirstName());
        sendKeys(createEnterpriseElements.getLastNameElement(), createEnterpriseInputs.getLastName());
        sendKeys(createEnterpriseElements.getPhoneElement(), createEnterpriseInputs.getPhone());
        sendKeys(createEnterpriseElements.getAddressElement(), createEnterpriseInputs.getAddress());
        sendKeys(createEnterpriseElements.getEmailElement(), "hakju.oh@gmail.com");//createEnterpriseInputs.getEmailAddress());

        Select purgeDurationInMonthsElement = createEnterpriseElements.getPurgeDurationInMonthsElement();
        purgeDurationInMonthsElement.selectByValue(Integer.toString(createEnterpriseInputs.getPurgeDurationInMonths()));

        WebElement signedAgreementElement = createEnterpriseElements.getSignedAgreementElement();
        if (createEnterpriseInputs.isSignedAgreement()) {
            signedAgreementElement.click();
        }

        createEnterpriseBtn = findElementByContainingId(webDriver, "button[type='submit']", "create_enterprise");
        createEnterpriseBtn.click();
    }
}
