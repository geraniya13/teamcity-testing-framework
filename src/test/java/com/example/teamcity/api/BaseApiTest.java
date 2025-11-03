package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.generators.random.TestDataGenerator;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseApiTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void apiBeforeMethod(Method method) {
        TestDataGenerator.applyUserRoles(testData, method);
    }
}
