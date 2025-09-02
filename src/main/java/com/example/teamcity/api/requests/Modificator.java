package com.example.teamcity.api.requests;


import io.restassured.specification.RequestSpecification;

public class Modificator {
    private ModificationStrategy modificationStrategy;

    public void setModificationStrategy(ModificationStrategy modificationStrategy) {
        this.modificationStrategy = modificationStrategy;
    }

    public RequestSpecification removeModification(RequestSpecification requestSpecification, String key) {
        return modificationStrategy.remove(requestSpecification, key);
    }

    public RequestSpecification updateModification(RequestSpecification requestSpecification, String value) {
        return modificationStrategy.update(requestSpecification, value);
    }


}
