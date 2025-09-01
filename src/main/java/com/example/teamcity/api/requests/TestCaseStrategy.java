package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public interface TestCaseStrategy<T> {
    //    String name();
//    default RequestSpecification prepareSpec(RequestSpecification base) { return base; }
//    default T prepareBody(T baseBody) { return baseBody; }
//
//    /** Выполнение через UNCHECKED – для негативов. */
//    default Response executeUnchecked (UncheckedRequests request,
//                                      Endpoint endpoint,
//                                      RequestSpecification baseSpec,
//                                      T baseBody)
//    {
//        var spec = prepareSpec(baseSpec);
//        var body = prepareBody(baseBody);
//        return request.withSpec(spec)
//                .getRequest(endpoint)
//                .createNullable((com.example.teamcity.api.models.BaseModel) body);
//    };
//
//    /** Проверки ответа (негатив/позитив переопределяют по-своему). */
//    void assertResponse(Response resp);
}
