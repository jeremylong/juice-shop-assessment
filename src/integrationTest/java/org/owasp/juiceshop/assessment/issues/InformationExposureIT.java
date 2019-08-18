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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.owasp.juiceshop.assessment.SeleniumTestBase;

/**
 *
 * @author Jeremy Long
 */
public class InformationExposureIT extends SeleniumTestBase {

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Directory listing should not be enalbed")
    public void directoryListingEnabled() {
        WebDriver driver = getDriver();
        driver.get("http://localhost:3000/#/about");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Check out our boring terms of use if you are interested in such lame stuff.")));
        driver.findElement(By.linkText("Check out our boring terms of use if you are interested in such lame stuff.")).click();
        assertThat(driver.getCurrentUrl(), containsString("/ftp/"));
        String ftpUrl = driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("/ftp/") + 4);
        driver.get(ftpUrl);
        assertThat("Directory listing should not be enabled", driver.getTitle(), not(containsString("listing directory")));
    }

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Confidential document `acquisitions.md` exposed")
    public void confidentialDocExposed() {

        WebDriver driver = getDriver();
        driver.get("http://localhost:3000/ftp/acquisitions.md");

        String confidentail = "This document is confidential! Do not distribute!";

        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + confidentail + "')]"));
        assertThat(list, hasSize(equalTo(0)));
    }
}
