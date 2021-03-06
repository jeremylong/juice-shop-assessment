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
package org.owasp.juiceshop.assessment.selenium;

import org.openqa.selenium.By;

/**
 *
 * @author Jeremy Long
 */
public class ByHelper {

    public static By buttonText(String text) {
        return By.xpath("//button[contains(text(), '" + text + "')]");
    }
    
        public static By buttonSpanText(String text) {
        return By.xpath("//span[contains(text(), '" + text + "')]");
    }

}
