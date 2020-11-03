/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) 2019 Jeremy Long. All Rights Reserved.
 */
package org.owasp.juiceshop.assessment;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Alert;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.owasp.juiceshop.assessment.selenium.ByHelper;

/**
 *
 * @author Jeremy Long
 */
public class SeleniumTestBase {

    private final String baseUrl = "http://kubernetes.docker.internal:3000";
    private BrowserMobProxy proxy;
    private Proxy seleniumProxy;
    private WebDriver driver;

    @BeforeAll
    public static void setUpClass() {
        //System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
        proxy = getProxyServer();
        seleniumProxy = getSeleniumProxy(proxy);
        ChromeOptions options = new ChromeOptions();
        options.setCapability("proxy", seleniumProxy);
        //options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        //options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        //options.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        proxy.stop();
        try {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        } catch (NoAlertPresentException ex) {
            //ignore
        }
        driver.close();
        driver.quit();
    }

    public BrowserMobProxy getProxy() {
        return proxy;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getFullUrl(String url) {
        if (url.length() == 1 && url.charAt(0) == '/' || url.length() > 1 && url.charAt(1) != '/') {
            return baseUrl + url;
        }
        return url;
    }

    public WebDriver get(String url) {
        driver.get(getFullUrl(url));
        return driver;
    }

    protected Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
        Proxy selProxy = ClientUtil.createSeleniumProxy(proxyServer);
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            selProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
            selProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
        } catch (UnknownHostException e) {
            fail("invalid Host Address:" + e.getMessage());
        }
        return selProxy;
    }

    protected BrowserMobProxy getProxyServer() {
        BrowserMobProxy mobProxy = new BrowserMobProxyServer();
        // trust applications with invalid certificates 
        mobProxy.setTrustAllServers(true);
        mobProxy.start();
        return mobProxy;
    }

    protected void login(WebDriver driver, String username, String password) {
        driver.get(getFullUrl("/#/login"));
        driver.findElement(By.name("email")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    protected void register(WebDriver driver, String user, String password) {
        driver.get(getFullUrl("/#/register"));
        clickJuiceShopPopups(driver);
        driver.findElement(By.id("emailControl")).sendKeys(user);
        driver.findElement(By.cssSelector("#mat-select-0 > div > div.mat-select-arrow-wrapper.ng-tns-c133-11")).click();
        driver.findElement(By.className("mat-option-text")).click();
        driver.findElement(By.id("securityAnswerControl")).sendKeys("bob");
        WebElement passwordControl = driver.findElement(By.id("passwordControl"));
        WebElement repeatPasswordControl = driver.findElement(By.id("repeatPasswordControl"));

        passwordControl.sendKeys(password);
        repeatPasswordControl.sendKeys(password);

        driver.findElement(By.id("registerButton")).click();
    }

    protected void clickJuiceShopPopups(WebDriver driver) {
        try {
            if (!driver.findElements(ByHelper.buttonSpanText("Dismiss")).isEmpty()) {
                clickPopupButton(driver, ByHelper.buttonSpanText("Dismiss"));
            }
            //clickPopupButton(driver, By.className("welcome-banner-close-button"));
            if (!driver.findElements( By.cssSelector("a.cc-dismiss")).isEmpty()) {
                clickPopupButton(driver, By.cssSelector("a.cc-dismiss"));
            }
            //clickPopupButton(driver, By.cssSelector("body > div.cc-window > div > a"));
        } catch (Throwable ignore) {}
    }

    private void clickPopupButton(WebDriver driver, By by) {
        (new WebDriverWait(driver, 5)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = null;
            try {
                e = d.findElement(by);
            } catch (Exception ex) {
                //ignore
            }
            if (e != null) {
                e.click();
            }
            return e != null;
        });
    }
}
