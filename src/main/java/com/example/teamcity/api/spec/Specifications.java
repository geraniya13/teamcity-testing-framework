package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.enums.Scope;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.User;
import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.filter.log.LogDetail.ALL;

public class Specifications {
    private static RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri("http://" + Config.getProperty("host")).build();
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        reqBuilder.log(ALL);
        return reqBuilder;
    }

    public static RequestSpecification defaulSpec() {
        return reqBuilder()
                .build();
    }

    public static RequestSpecification superUserSpec() {
        return reqBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri("http://%s:%s@%s".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification unauthSpec() {
        return reqBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification authSpec(User user) {
        BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
        basicAuthScheme.setUserName(user.getUsername());
        basicAuthScheme.setPassword(user.getPassword());

        return reqBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setAuth(basicAuthScheme)
                .build();
    }

    public static RequestSpecification mockSpec() {
        return reqBuilder()
                .setBaseUri("http://localhost:8081")
                .build();
    }
}
