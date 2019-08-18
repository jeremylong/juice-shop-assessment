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
package org.owasp.juiceshop.assessment.issues;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.owasp.juiceshop.assessment.SeleniumTestBase;

/**
 *
 * @author Jeremy Long
 */
public class TrackOrdersReflectedXssIT extends SeleniumTestBase {

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Insufficient/missing output encoding allows reflected XSS")
    public void testReflectedXss() throws Exception {

        WebDriver driver = getDriver();
        String username = "someone@gmail.com";
        String password = "12345";

        register(driver, username, password);
        login(driver, username, password);

        driver.findElement(By.id("userMenuButton")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("#cdk-overlay-2 > div > div > button:nth-child(4)")).click();

        Thread.sleep(1000);
        driver.findElement(By.id("orderId")).sendKeys("<iframe src=\"javascript:alert(`xss`)\">");
        driver.findElement(By.id("trackButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5 /*timeout in seconds*/);
        if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
            fail("Alert dialog box should not be present - ensure input is validated and output is encoded");
        }
    }
}
