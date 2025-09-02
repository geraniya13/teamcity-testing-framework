package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.enums.Scope;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.User;
import io.restassured.RestAssured;
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
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setAccept(ContentType.JSON);
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        reqBuilder.log(ALL);
        return reqBuilder;
    }

    public static RequestSpecification superUserSpec() {
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification unauthSpec() {
        return reqBuilder().build();
    }

    public static RequestSpecification authSpec(User user) {
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification mockSpec() {
        return reqBuilder()
                .setBaseUri("http://localhost:8081")
                .build();
    }

    /** Скопировать базовую спеку и удалить заголовок. */
    public static RequestSpecification removeHeader(RequestSpecification base, String header) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base)
                .addHeader(header, (String) null)
                .build();
    }

    /** Скопировать базовую спеку и переопределить заголовок. */
    public static RequestSpecification setHeader(RequestSpecification base, String header, String value) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base)
                .addHeader(header, value)
                .build();
    }

    /** Поставить конкретный Content-Type. */
    public static RequestSpecification withContentType(RequestSpecification base, String mime) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base)
                .setContentType(mime)
                .build();
    }
}
