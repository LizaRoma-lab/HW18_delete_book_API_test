package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Configuration.browserSize;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {
    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--user-data-dir=/tmp/chrome-profile-" + System.currentTimeMillis(),
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-gpu",
                "--window-size=1920,1080"
        );
        Configuration.browserCapabilities = options;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
        );
    }

    @BeforeEach
    void setUp() {
        pageLoadStrategy = "eager"; // не ждать полной загрузки страницы
        timeout = 10000; // 10 секунд для ожидания элементов
        pageLoadTimeout = 30000; // 30 секунд для загрузки страницы
        browserSize = "1920x1080";
    }

    @AfterEach
    void shutDown() {
        closeWebDriver();
    }
}
