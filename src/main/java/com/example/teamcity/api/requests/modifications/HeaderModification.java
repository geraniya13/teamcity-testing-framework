package com.example.teamcity.api.requests.modifications;

import com.example.teamcity.api.requests.ModificationStrategy;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class HeaderModification implements ModificationStrategy {
    RequestSpecBuilder builder = new RequestSpecBuilder();
    FilterableRequestSpecification filter;

    @Override
    public Object remove(RequestSpecification requestSpecification, String key) {
        return (requestSpecification).filter((req, res, ctx) -> {
                    req.removeHeader(key); // меняем что хотим
                    return ctx.next(req, res);         // отправляем дальше
                });
    }

    @Override
    public Object update(RequestSpecification requestSpecification, String value) {
        return null;
    }

    RequestSpecification req = new HeaderModification().remove(new RequestSpecification(), "key");

}
