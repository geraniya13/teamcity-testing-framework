package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

public interface CrudInterface {
    Object create(Object model, String... args);
    Object read(String id,String... args);
    Object update(String id, BaseModel model, String... args);
    Object delete(String id, String... args);
}
