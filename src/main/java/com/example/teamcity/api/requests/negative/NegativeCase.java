package com.example.teamcity.api.requests.negative;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.TestCaseStrategy;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;


public abstract class NegativeCase<T> implements TestCaseStrategy<T> {
    public NegativeCase (int status, String error, String msg) {
        this.status = status;
        this.error = error;
        this.msg = msg;
    }

    private int status;
    private String error,
            msg;

    public int expectedStatus() { return status;}

    public String expectedError() { return error;}

    public String expectedMessage() { return msg;}

    @Override
    public void assertResponse(Response response) {
        if (response.getStatusCode() != expectedStatus()) {
            throw new AssertionError(String.format("Expected %s, got %d body=%s" + response.asString(), expectedStatus(), response.getStatusCode(), response.getBody().asString()));
        }
    }

    private void runUnchecked(Endpoint endpoint, RequestSpecification spec, TestCaseStrategy<BaseModel> s, BaseModel baseModel) {
            var client   = new UncheckedRequests(spec);

            Response resp = s.executeUnchecked(
                    client,
                    endpoint,
                    spec,
                    baseModel
            );
            s.assertResponse(resp);
        ;
    }
}
