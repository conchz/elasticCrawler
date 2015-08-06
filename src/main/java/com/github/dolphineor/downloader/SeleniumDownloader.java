package com.github.dolphineor.downloader;

import com.github.dolphineor.scheduler.Task;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@code SeleniumDownloader} is used for downloading the pages via AJAX rendering.
 *
 * @author dolphineor
 */
public class SeleniumDownloader implements Downloader {
    @Override
    public String download(Task task) throws IOException {
        Map<String, Object> contentSettings = new HashMap<>();
        contentSettings.put("images", 2);

        Map<String, Object> preferences = new HashMap<>();
        preferences.put("profile.default_content_settings", contentSettings);

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("chrome.prefs", preferences);
        WebDriver webDriver = new ChromeDriver(caps);
        webDriver.get(task.getUrl());
        sleep(1, TimeUnit.SECONDS);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        webDriver.close();
        return webElement.getAttribute("outerHTML");
    }

    private void sleep(long time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
