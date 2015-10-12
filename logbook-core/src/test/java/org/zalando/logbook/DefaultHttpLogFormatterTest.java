package org.zalando.logbook;

/*
 * #%L
 * Logbook: Core
 * %%
 * Copyright (C) 2015 Zalando SE
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;
import org.zalando.logbook.DefaultLogbook.SimpleCorrelation;
import org.zalando.logbook.DefaultLogbook.SimplePrecorrelation;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public final class DefaultHttpLogFormatterTest {

    private final HttpLogFormatter unit = new DefaultHttpLogFormatter();

    @Test
    public void shouldLogRequest() throws IOException {
        final String correlationId = "c9408eaa-677d-11e5-9457-10ddb1ee7671";
        final HttpRequest request = MockHttpRequest.builder()
                .requestUri("/test?limit=1")
                .header("Accept", "application/json")
                .header("Content-Type", "text/plain")
                .body("Hello, world!")
                .build();

        final String http = unit.format(new SimplePrecorrelation<>(correlationId, request));

        assertThat(http, equalTo("GET /test?limit=1 HTTP/1.1\n" +
                "Accept: application/json\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello, world!"));
    }

    @Test
    public void shouldLogRequestWithoutQueryParameters() throws IOException {
        final String correlationId = "2bd05240-6827-11e5-bbee-10ddb1ee7671";
        final HttpRequest request = MockHttpRequest.builder()
                .requestUri("/test")
                .header("Accept", "application/json")
                .header("Content-Type", "text/plain")
                .body("Hello, world!")
                .build();

        final String http = unit.format(new SimplePrecorrelation<>(correlationId, request));

        assertThat(http, equalTo("GET /test HTTP/1.1\n" +
                "Accept: application/json\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello, world!"));
    }

    @Test
    public void shouldLogRequestWithoutBody() throws IOException {
        final String correlationId = "0eae9f6c-6824-11e5-8b0a-10ddb1ee7671";
        final HttpRequest request = MockHttpRequest.builder()
                .requestUri("/test")
                .header("Accept", "application/json")
                .build();

        final String http = unit.format(new SimplePrecorrelation<>(correlationId, request));

        assertThat(http, equalTo("GET /test HTTP/1.1\n" +
                "Accept: application/json"));
    }

    @Test
    public void shouldLogResponse() throws IOException {
        final String correlationId = "2d51bc02-677e-11e5-8b9b-10ddb1ee7671";
        final HttpRequest request = MockHttpRequest.create();
        final HttpResponse response = MockHttpResponse.builder()
                .header("Content-Type", "application/json")
                .body("{\"success\":true}")
                .build();

        final String http = unit.format(new SimpleCorrelation<>(correlationId, request, response));

        assertThat(http, equalTo("HTTP/1.1 200\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "{\"success\":true}"));
    }

    @Test
    public void shouldLogResponseWithoutBody() throws IOException {
        final String correlationId = "3881ae92-6824-11e5-921b-10ddb1ee7671";
        final HttpRequest request = MockHttpRequest.create();
        final HttpResponse response = MockHttpResponse.builder()
                .header("Content-Type", "application/json")
                .build();

        final String http = unit.format(new SimpleCorrelation<>(correlationId, request, response));

        assertThat(http, equalTo("HTTP/1.1 200\n" +
                "Content-Type: application/json"));
    }

}