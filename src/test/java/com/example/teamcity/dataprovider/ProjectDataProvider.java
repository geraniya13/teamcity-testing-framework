package com.example.teamcity.dataprovider;


import com.example.teamcity.api.models.User;
import org.testng.annotations.DataProvider;


public class ProjectDataProvider {
    @DataProvider(name = "project_headers_negative", parallel = true)
    public static Object[][] headersNegative() {
        return new Object[][]{
                {new User(), "Authorization", 401, "Authentication required"}
//                , "No bearer token provided"},
//                {"Content-Type",   415, "Content-Type header is required"}
//                { new InvalidContentTypeCase("text/plain",  415, "Unsupported Media Type", "Expected application/json") }
        };
    }

    @DataProvider(name = "project_body_negative", parallel = true)
    public static Object[][] bodyNegative() {
        return new Object[][]{
                { 400, "Bad Request", "Request body is empty" }
        };
    }


}
