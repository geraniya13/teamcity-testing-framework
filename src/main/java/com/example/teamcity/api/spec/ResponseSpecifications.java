package com.example.teamcity.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import static io.restassured.filter.log.LogDetail.ALL;

public class ResponseSpecifications {
    private static ResponseSpecBuilder respBuilder() {
        ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
        respBuilder.log(ALL);
        return respBuilder;
    };

    public static ResponseSpecification generalValidationRespSpec(Integer status, String expectedFormat, String... expectedArgs) {
        return respBuilder()
                .expectStatusCode(status)
                .expectBody(Matchers.containsString(expectedFormat.formatted(expectedArgs)))
                .build();
    }
}
