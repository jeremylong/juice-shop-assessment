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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import org.owasp.juiceshop.assessment.SeleniumTestBase;
import org.owasp.juiceshop.assessment.proxy.HttpStatusFilter;

/**
 *
 * @author Jeremy Long
 */
public class InformationExposureIT extends SeleniumTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(InformationExposureIT.class);

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Directory listing should not be enabled")
    public void directoryListingEnabled() {
        WebDriver driver = get("/#/about");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement link = wait.until(presenceOfElementLocated(By.linkText("Check out our boring terms of use if you are interested in such lame stuff.")));
        link.click();
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
        String acquisitions = "/ftp/acquisitions.md";

        HttpStatusFilter statusMap = new HttpStatusFilter();
        getProxy().addResponseFilter(statusMap);

        WebDriver driver = get(acquisitions);

        String confidentail = "This document is confidential!";
        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + confidentail + "')]"));

        assertThat("No confidential documents should be publicly available", list, hasSize(equalTo(0)));

        assertThat("The aqcuisitions document should not be publicly available", statusMap.getStatus(getFullUrl(acquisitions)), not(equalTo(200)));
    }
}
