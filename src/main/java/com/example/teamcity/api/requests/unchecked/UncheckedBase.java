package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.SearchInterface;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UncheckedBase extends Request implements CrudInterface, SearchInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response create(Object model, String... args) {
            return RestAssured
                    .given()
                    .spec(spec)
                    .body(model)
                    .post( endpoint.getUrl() + (args.length < 1 ? "" : String.join("", args)));
    }

    @Override
    public Response read(String id, String... args) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/id:" + id + (args.length < 1 ? "" : String.join("", args)));
    }

    @Override
    public Response update(String id, BaseModel model, String... args) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .put(endpoint.getUrl() + "/id:" + id + (args.length < 1 ? "" : String.join("", args)));
    }

    @Override
    public Response delete(String id, String... args) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/id:" + id + (args.length < 1 ? "" : String.join("", args)));
    }

    @Override
    public Response searchSingle(String[]... args) {
        var queryParams = Arrays.stream(args)
                .map(pair -> String.join(":", pair))
                .collect(Collectors.joining(",", "/", ""));

        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + queryParams);
    }

    @Override
    public Response searchAll(String[]... args) {
        var queryParams = Arrays.stream(args)
            .map(pair -> String.join(":", pair))
            .collect(Collectors.joining(",", "?", ""));

        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + queryParams);
    }
}
