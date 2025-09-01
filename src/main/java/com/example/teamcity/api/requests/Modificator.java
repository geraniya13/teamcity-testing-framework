package com.example.teamcity.api.requests;


import io.restassured.specification.RequestSpecification;

public class Modificator {
    private ModificationStrategy modificationStrategy;

    public void setModificationStrategy(ModificationStrategy modificationStrategy) {
        this.modificationStrategy = modificationStrategy;
    }

    public void removeModification(RequestSpecification requestSpecification, String key) {
        modificationStrategy.remove(requestSpecification, key);
    }

    public void updateModification(RequestSpecification requestSpecification, String value) {
        modificationStrategy.update(requestSpecification, value);
    }


}
