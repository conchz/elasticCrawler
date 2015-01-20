package org.elasticcrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by dolphineor on 2015-1-20.
 */
public class WebDriverTest {

    public static void main(String[] args) {
        Map<String, Object> contentSettings = new HashMap<>();
        contentSettings.put("images", 2);

        Map<String, Object> preferences = new HashMap<>();
        preferences.put("profile.default_content_settings", contentSettings);

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("chrome.prefs", preferences);
        WebDriver webDriver = new ChromeDriver(caps);
        webDriver.get("http://m.ctrip.com/webapp/hotel/citylist");
        sleep(1);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        System.out.println(webElement.getAttribute("outerHTML"));
        webDriver.close();
    }

    public static void sleep(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
