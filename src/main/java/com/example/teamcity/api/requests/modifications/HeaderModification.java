package com.example.teamcity.api.requests.modifications;

import com.example.teamcity.api.requests.ModificationStrategy;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static io.restassured.RestAssured.given;

public class HeaderModification implements ModificationStrategy {
    RequestSpecBuilder builder = new RequestSpecBuilder();
    FilterableRequestSpecification filter;

    @Override
    public RequestSpecification remove(RequestSpecification spec, String key) {
        return spec.noContentType();
    }

    @Override
    public Object update(RequestSpecification requestSpecification, String value) {
        return null;
    }

//    RequestSpecification req = new HeaderModification().remove(new RequestSpecification(), "key");

}
