package com.example.teamcity.api.requests.cases;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.negative.NegativeCase;
import io.restassured.specification.RequestSpecification;

public class EmptyBodyCase extends NegativeCase<BaseModel> {

    public EmptyBodyCase(int status, String error, String msg) {
        super(status, error, msg);
    }

    @Override
    public String name() { return "Empty body"; }


    public NewProjectDescription prepareBody(NewProjectDescription base) {
        return null; // отправляем совсем без body
    }
}
