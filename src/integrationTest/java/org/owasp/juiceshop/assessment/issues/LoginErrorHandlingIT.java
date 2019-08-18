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
public class LoginErrorHandlingIT extends RestAssuredTestBase {

    @Test
    @Tag("security")
    @Tag("integrationTest")
    @DisplayName("Do not expose detailed error messages")
    public void doNotReturnDetailedErrorMessages() {

        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "'");
        requestParams.put("password", "AnyPa$$wordWillD0");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestParams.toString())
                .when()
                .post("/rest/user/login")
                .then()
                //.log().all()
                .statusCode(500)
                .body("error.original.code", not(containsString("SQLITE_ERROR")))
                .body("error.sql", not(containsString("SELECT ")))
                .body("error.parent.sql", not(containsString("SELECT ")))
                .body("error.original.sql", not(containsString("SELECT ")));
    }
}
