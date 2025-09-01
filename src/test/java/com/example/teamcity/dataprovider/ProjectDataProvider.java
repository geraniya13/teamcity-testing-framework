package com.example.teamcity.dataprovider;


import com.example.teamcity.api.requests.cases.EmptyBodyCase;
import com.example.teamcity.api.requests.cases.InvalidContentTypeCase;
import com.example.teamcity.api.requests.cases.MissingHeaderCase;
import com.example.teamcity.api.requests.cases.ValidCase;
import org.testng.annotations.DataProvider;


public class ProjectDataProvider {
    @DataProvider(name = "project_headers_negative", parallel = true)
    public static Object[][] headersNegative() {
        return new Object[][]{
                { new MissingHeaderCase("Authorization", 401, "Authentication required", "No bearer token provided") },
                { new MissingHeaderCase("Content-Type",   415, "Unsupported Media Type", "Content-Type header is required") },
                { new InvalidContentTypeCase("text/plain",  415, "Unsupported Media Type", "Expected application/json") }
        };
    }

    @DataProvider(name = "project_body_negative", parallel = true)
    public static Object[][] bodyNegative() {
        return new Object[][]{
                { new EmptyBodyCase(400, "Bad Request", "Request body is empty") }
        };
    }

    @DataProvider(name = "project_positive", parallel = true)
    public static Object[][] positive() {
        return new Object[][]{
                { new ValidCase() }
        };
    }
}
