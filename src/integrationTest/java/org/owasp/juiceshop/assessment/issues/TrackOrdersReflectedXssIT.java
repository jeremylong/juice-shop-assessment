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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.owasp.juiceshop.assessment.SeleniumTestBase;
import org.owasp.juiceshop.assessment.selenium.ByHelper;

/**
 *
 * @author Jeremy Long
 */
public class TrackOrdersReflectedXssIT extends SeleniumTestBase {

    //track orders moved?
    //@Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Insufficient/missing output encoding allows reflected XSS")
    public void testReflectedXss() throws Exception {

        WebDriver driver = getDriver();
        String username = "someone@gmail.com";
        String password = "12345";

        register(driver, username, password);
        login(driver, username, password);

        WebDriverWait wait = new WebDriverWait(driver, 5);
        
        driver.findElement(By.id("userMenuButton")).click();
        WebElement element = wait.until(presenceOfElementLocated(ByHelper.buttonText("Track Orders")));
        element.click();

        element = wait.until(presenceOfElementLocated(By.id("orderId")));
        element.sendKeys("<iframe src=\"javascript:alert(`xss`)\">");
        
        driver.findElement(By.id("trackButton")).click();
        
        Alert alert = wait.until(alertIsPresent());
        assertThat("An alert dialog box should not be present - ensure input is validated and output is encoded",
            alert, nullValue());
    }
}
