package com.example.teamcity.api.requests;

import io.restassured.specification.RequestSpecification;

public interface ModificationStrategy {
    Object remove(RequestSpecification requestSpecification, String key);
    Object update(RequestSpecification requestSpecification, String value);
}
