package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.demoqa.helpers.Attachments;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {
    @BeforeAll
    static void setup() {

        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";


        Configuration.browser = System.getProperty("browser", "chrome");;
        Configuration.browserSize = System.getProperty("screenResolution", "1920x1080");

        String browserVersion = System.getProperty("browserVersion");
        if (browserVersion != null) {
            Configuration.browserVersion = browserVersion;
        }

        String remoteUrl = System.getProperty("remoteUrl");
        if (remoteUrl != null) {
            Configuration.remote = remoteUrl;

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));

            if (browserVersion != null) {
                capabilities.setCapability("browserVersion", browserVersion);
            }

            Configuration.browserCapabilities = capabilities;
        }
    }

    @BeforeEach
    void initListeners() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attachments.screenshotAs("Last screenshot");
        Attachments.pageSource();
        Attachments.browserConsoleLogs();
        Attachments.addVideo();
        Selenide.closeWebDriver();
    }
}