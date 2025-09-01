package com.example.teamcity.api.requests.cases;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.negative.NegativeCase;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;

public class InvalidContentTypeCase extends NegativeCase<BaseModel> {

    private final String mime;

    public InvalidContentTypeCase(String mime, int status, String error, String msg) {
        super(status, error, msg);
        this.mime = mime;
    }

    @Override public String name() { return "Wrong Content-Type: " + mime; }

    @Override
    public RequestSpecification prepareSpec(RequestSpecification base) {
        return Specifications.withContentType(base, mime);
    }
}
