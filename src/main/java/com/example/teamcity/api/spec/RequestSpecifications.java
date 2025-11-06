package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;
import java.util.List;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;
import static io.restassured.filter.log.LogDetail.ALL;

public class RequestSpecifications {
    private static RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri("http://" + Config.getProperty("host")).build();
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        reqBuilder.addFilter(new SwaggerCoverageRestAssured(
                new FileSystemOutputWriter(
                        Paths.get("target/" + OUTPUT_DIRECTORY)
                )
        ));
        reqBuilder.log(ALL);
        return reqBuilder;
    }

    public static RequestSpecification defaultSpec() {
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
