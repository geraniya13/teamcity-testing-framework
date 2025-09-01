package com.example.teamcity.api.requests.cases;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.negative.NegativeCase;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;

public class MissingHeaderCase extends NegativeCase<BaseModel> {
    private final String header;

    public MissingHeaderCase(String header, int status, String error, String msg) {
        super(status, error, msg);
        this.header = header;
    }

    @Override
    public String name() {
        return "Missing header: " + header;
    }

    @Override
    public RequestSpecification prepareSpec(RequestSpecification base) {
        return Specifications.removeHeader(base, header);
    }
}
