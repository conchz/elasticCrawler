package org.spider.downloader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.spider.scheduler.Task;
import org.spider.util.Logs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@code SeleniumDownloader} is used for downloading the pages via AJAX rendering.
 *
 * @author dolphineor
 */
public class SeleniumDownloader extends Logs implements Downloader {

    private static Downloader downloader;


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
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        webDriver.close();
        return webElement.getAttribute("outerHTML");
    }

    public static Downloader create() {
        if (downloader == null) {
            synchronized (SeleniumDownloader.class) {
                if (downloader == null)
                    downloader = new SeleniumDownloader();
            }
        }
        return downloader;
    }

}
