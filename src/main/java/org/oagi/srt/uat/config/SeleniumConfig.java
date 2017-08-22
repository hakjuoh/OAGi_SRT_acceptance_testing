package org.oagi.srt.uat.config;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class SeleniumConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CHROME_DRIVER_PROPERTY_KEY = "webdriver.chrome.driver";

    @Value("${os.name}")
    private String osName;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    @Scope("prototype")
    public WebDriver webDriver() throws IOException {
        if (StringUtils.isEmpty(osName)) {
            throw new IllegalStateException("Can't read 'os.name' system property.");
        }

        logger.debug("'os.name' property: " + this.osName);

        String osName = this.osName.toLowerCase();
        Resource driverResource;
        if (osName.contains("win")) {
            driverResource = resourceLoader.getResource("classpath:/chromedriver.exe");
        } else if (osName.contains("mac")) {
            driverResource = resourceLoader.getResource("classpath:/chromedriver");
        } else {
            throw new UnsupportedOperationException("We support only Windows or Mac OS X Operating Systems.");
        }

        String webDriverFilePath = driverResource.getFile().getCanonicalPath();
        logger.debug("Chrome WebDriver Path: " + webDriverFilePath);

        System.setProperty(CHROME_DRIVER_PROPERTY_KEY, webDriverFilePath);

        ChromeDriver driver = new ChromeDriver();

        WebDriver.Timeouts timeouts = driver.manage().timeouts();
        timeouts.pageLoadTimeout(10L, TimeUnit.SECONDS);

        return driver;
    }
}
