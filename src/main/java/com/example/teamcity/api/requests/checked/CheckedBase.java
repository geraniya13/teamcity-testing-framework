package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.SearchInterface;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;

@SuppressWarnings("unchecked")
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface, SearchInterface {
    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public T create(Object model, String... args) {
        var createdModel = (T) uncheckedBase
                .create(model, args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        // мы уверены, что сущность создана
        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String id, String... args) {
        return (T) uncheckedBase
                .read(id, args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public T update(String id, BaseModel model, String... args) {
        return (T) uncheckedBase
                .update(id, model, args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public Object delete(String id,  String... args) {
        return uncheckedBase
                .delete(id, args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }

    @Override
    public Object searchAll(String[]...args) {
        return uncheckedBase
                .searchAll(args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<List<T>>() {});
    }

    @Override
    public T searchSingle(String[]... args) {
        return (T) uncheckedBase
                .searchSingle(args)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }
}
