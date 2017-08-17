package org.oagi.srt.uat.testcase.phase5;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oagi.srt.uat.testcase.CreateAccountInputs;
import org.oagi.srt.uat.testcase.CreateEnterpriseInputs;
import org.oagi.srt.uat.testcase.UserRole;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.oagi.srt.uat.testcase.TestCaseHelper.*;
import static org.oagi.srt.uat.testcase.phase2.TestCase2_Helper.createEnterpriseAccount;
import static org.oagi.srt.uat.testcase.phase3.TestCase3_Helper.createEnterprise;
import static org.oagi.srt.uat.testcase.phase5.TestCase5_14.changeEnterpriseUserRole;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCase5_22 {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebDriver webDriver_1, webDriver_2;

    @Autowired
    private Random random;

    private CreateEnterpriseInputs enterprise;
    private CreateAccountInputs enterpriseAdmin_1, enterpriseAdmin_2;

    @Before
    public void setUp() {
        enterprise = createEnterprise(webDriver_1, random, CreateAccountInputs.OAGI_ADMIN);

        enterpriseAdmin_1 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver_1, enterpriseAdmin_1, enterprise, UserRole.AdminUser);

        enterpriseAdmin_2 = CreateAccountInputs.generateRandomly(random);
        createEnterpriseAccount(webDriver_1, enterpriseAdmin_2, enterprise, UserRole.AdminUser);

        logout(webDriver_1);

        login(webDriver_1, enterpriseAdmin_1);
        login(webDriver_2, enterpriseAdmin_2);
    }

    @After
    public void tearDown() {
        webDriver_1.close();
        webDriver_2.close();
    }
    
    @Test
    public void testRevokeAdminUserRoleToAnotherAdminUserByEnterpriseAdmin() {
        gotoSubMenu(webDriver_1, "Admin", "Manage Tenant’s Users");
        gotoSubMenu(webDriver_2, "Admin", "Manage Tenant’s Users");

        changeEnterpriseUserRole(webDriver_1, enterpriseAdmin_2.getLoginId(), "Revoke");
        changeEnterpriseUserRole(webDriver_2, enterpriseAdmin_1.getLoginId(), "Revoke");

        String errorMessage = getErrorMessage(webDriver_2);
        logger.info("Error Message: " + errorMessage);

        assertTrue(!StringUtils.isEmpty(errorMessage));
    }
}
