package com.example.teamcity.api.requests.positive;

import com.example.teamcity.api.requests.TestCaseStrategy;
import io.restassured.response.Response;

public abstract class PositiveCase<T> implements TestCaseStrategy<T> {
    public abstract String name();

    @Override
    public void assertResponse(Response response) {
        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new AssertionError("Expected 2xx, got " + response.getStatusCode() + " body=" + response.asString());
        }
    }
}
