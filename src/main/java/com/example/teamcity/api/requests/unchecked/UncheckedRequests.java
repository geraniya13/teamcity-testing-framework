package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.requests.TestCaseStrategy;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;

public class UncheckedRequests implements TestCaseStrategy {
    private final EnumMap<Endpoint, UncheckedBase> requests = new EnumMap<>(Endpoint.class);

    public UncheckedRequests(RequestSpecification spec) {
        for (var endpoint: Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    public UncheckedBase getRequest(Endpoint endpoint) {
        return requests.get(endpoint);
    }

    /** Новый: вернуть новый контейнер с другой спекой (для «сломанных» заголовков). */
    public UncheckedRequests withSpec(RequestSpecification override) {
        return new UncheckedRequests(override);
    }

    @Override
    public void modify(Endpoint endpoint, Modification modification) {
        requests.put(endpoint, new UncheckedBase(modification.modify(requests.get(endpoint)), endpoint));
    }
}
