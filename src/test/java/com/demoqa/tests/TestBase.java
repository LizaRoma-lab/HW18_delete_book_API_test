package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.demoqa.helpers.Attachments;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {
    @BeforeAll
    static void setup() {
        // Базовые настройки
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";

        // Настройки удаленного драйвера
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";

        // SSL настройки
        System.setProperty("selenide.remote.allowInsecure", "true");
        System.setProperty("selenide.remote.ssl-protocol", "TLSv1.2");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void initListeners() {
        Attachments.initSelenideListener(); // Инициализация Allure listener
    }

    @AfterEach
    void addAttachments() {
        Attachments.addScreenshot();
        Attachments.addPageSource();
        Attachments.addBrowserConsoleLogs();
        Attachments.addVideo();
        Selenide.closeWebDriver();
    }
}