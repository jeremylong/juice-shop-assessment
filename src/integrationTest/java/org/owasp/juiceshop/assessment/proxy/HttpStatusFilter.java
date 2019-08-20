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
package org.owasp.juiceshop.assessment.proxy;

import io.netty.handler.codec.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 *
 * @author Jeremy Long
 */
public class HttpStatusFilter implements ResponseFilter {

    Map<String, Integer> status = new HashMap<>();

    @Override
    public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
        status.put(messageInfo.getOriginalUrl(), response.getStatus().code());
    }
    
    public int getStatus(String url) {
        return status.get(url);
    }
}
