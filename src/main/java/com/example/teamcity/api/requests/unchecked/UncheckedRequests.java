package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.requests.ModificationStrategy;
import com.example.teamcity.api.requests.Modificator;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;

public class UncheckedRequests {
    private final Modificator modificator = new Modificator();
    private final EnumMap<Endpoint, UncheckedBase> requests = new EnumMap<>(Endpoint.class);

    public UncheckedRequests(RequestSpecification spec) {
        for (var endpoint : Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    public UncheckedBase getRequest(Endpoint endpoint) {
        return requests.get(endpoint);
    }


    public void modify(Endpoint endpoint, ModificationStrategy modificationStrategy, String action, String key, String[] value) {
        UncheckedBase newUncheckedBase = null;
        RequestSpecification spec = requests.get(endpoint).getSpec();
        modificator.setModificationStrategy(modificationStrategy);
        switch (action) {
            case "remove":
                newUncheckedBase = new UncheckedBase(modificator.removeModification(spec, key), endpoint);
                requests.replace(endpoint, newUncheckedBase);
                break;
            case "update":
                newUncheckedBase = new UncheckedBase(modificator.updateModification(spec, key, value), endpoint);
                break;
            default:
                requests.replace(endpoint, newUncheckedBase);
        }
    }
}
