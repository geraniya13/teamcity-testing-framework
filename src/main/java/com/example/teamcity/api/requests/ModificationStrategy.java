package com.example.teamcity.api.requests;

import io.restassured.specification.RequestSpecification;

public interface ModificationStrategy {
    <T extends Object> T remove(RequestSpecification requestSpecification, String key);
    <T extends Object> T update(RequestSpecification requestSpecification, String key, String[]... values);
}
