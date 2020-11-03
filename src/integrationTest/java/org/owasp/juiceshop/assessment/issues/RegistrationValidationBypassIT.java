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
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.owasp.juiceshop.assessment.SeleniumTestBase;

/**
 *
 * @author Jeremy Long
 */
public class RegistrationValidationBypassIT extends SeleniumTestBase {

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Repeat password validation can be bypassed")
    public void testPasswordsDoNotMatch() throws Exception {

        WebDriver driver = get("/#/register");
        clickJuiceShopPopups(driver);
        driver.findElement(By.id("emailControl")).sendKeys("someone@gmail.com");
        driver.findElement(By.cssSelector("#mat-select-0 > div > div.mat-select-arrow-wrapper.ng-tns-c133-11")).click();
        driver.findElement(By.className("mat-option-text")).click();
        driver.findElement(By.id("securityAnswerControl")).sendKeys("bob");
        WebElement passwordControl = driver.findElement(By.id("passwordControl"));
        WebElement repeatPasswordControl = driver.findElement(By.id("repeatPasswordControl"));

        passwordControl.sendKeys("12345");
        repeatPasswordControl.sendKeys("12345");

        //cause blur
        driver.findElement(By.id("emailControl")).click();

        assertThat("The registration button should be enabled because the passwords match and all fields are completed correctly",
                driver.findElement(By.id("registerButton")).isEnabled(), is(true));

        passwordControl.clear();
        passwordControl.sendKeys("AnyPa$$wordWillD0");

        assertThat("The registration button should be disabled because the passwords do not match",
                driver.findElement(By.id("registerButton")).isEnabled(), is(false));
    }
}
