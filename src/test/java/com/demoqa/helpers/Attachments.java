package com.demoqa.helpers;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class Attachments {
    public static void initSelenideListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    public static void addScreenshot() {
        if (getWebDriver() instanceof TakesScreenshot) {
            Allure.addAttachment(
                    "Screenshot",
                    "image/png",
                    new ByteArrayInputStream(((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES)),
                    "png"
            );
        }
    }

    public static void addPageSource() {
        Allure.addAttachment(
                "Page source",
                "text/html",
                getWebDriver().getPageSource(),
                "html"
        );
    }

    public static void addBrowserConsoleLogs() {
        Allure.addAttachment(
                "Browser console logs",
                "text/plain",
                String.join("\n", Selenide.getWebDriverLogs("browser")),
                "txt"
        );
    }

    public static void addVideo() {
        try {
            if (getWebDriver() instanceof RemoteWebDriver) {
                String sessionId = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
                String videoUrl = "https://selenoid.autotests.cloud/video/" + sessionId + ".mp4";

                Allure.addAttachment(
                        "Video",
                        "text/html",
                        "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                                + videoUrl + "' type='video/mp4'></video></body></html>",
                        "html"
                );
            }
        } catch (Exception e) {
            System.out.println("Не удалось добавить видео: " + e.getMessage());
        }
    }
}
