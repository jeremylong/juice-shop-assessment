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

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.owasp.juiceshop.assessment.RestAssuredTestBase;

/**
 *
 * @author Jeremy Long
 */
public class ProductsSearchIT extends RestAssuredTestBase {
    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Prevent SQL Injection - consider using bound parameters")
    public void preventSqlInjection() {

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("q", "qwert')) UNION SELECT '1', id, email, password, '5', '6', '7', '8' FROM Users--")
                .when()
                .get("/rest/products/search")
                .then()
                .log().all()
                .body("data.find { it.name == '1' }.description", not(containsString("admin@juice-sh.op")))
                .body("data[0].description", not(containsString("admin@juice-sh.op")))
                ;
    }
}
