package com.example.teamcity.api.requests.modifications;

import com.example.teamcity.api.requests.ModificationStrategy;
import io.restassured.specification.RequestSpecification;

public class BodyModification implements ModificationStrategy {
    @Override
    public Object remove(RequestSpecification requestSpecification, String key) {
        return null;
    }

    @Override
    public Object update(RequestSpecification requestSpecification, String key, String[]... values) {
        return null;
    }
}
