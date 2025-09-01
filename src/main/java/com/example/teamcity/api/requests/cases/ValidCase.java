package com.example.teamcity.api.requests.cases;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.positive.PositiveCase;

public class ValidCase extends PositiveCase<BaseModel> {
    @Override public String name() { return "Successful validation"; }
}
