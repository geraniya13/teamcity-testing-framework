package com.example.teamcity;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.generators.random.TestDataGenerator;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.spec.RequestSpecifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

import static com.example.teamcity.api.generators.random.TestDataGenerator.generate;


public class BaseTest {
    protected SoftAssert softy;
    protected CheckedRequests superUserCheckRequests = new CheckedRequests(RequestSpecifications.superUserSpec());
    protected TestData testData;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest(Method method) {
        softy = new SoftAssert();
        testData = generate();
            TestDataGenerator.applyUserRoles(testData, method);
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getStorage().deleteCreatedEntities();
    }
}
