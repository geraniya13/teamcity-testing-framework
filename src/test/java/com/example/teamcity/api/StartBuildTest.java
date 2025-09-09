package com.example.teamcity.api;

import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.common.WireMock;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.example.teamcity.api.enums.Endpoint.BUILD_QUEUE;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {
    @BeforeMethod
    public void setupWireMockServer() {
        testData.getBuild().setState("finished");
        testData.getBuild().setStatus("SUCCESS");

        WireMock.setupServer(post(BUILD_QUEUE.getUrl()), HttpStatus.SC_OK, testData.getBuild());
    }

    @Test(description = "User should be able to start build (with WireMock)", groups = {"Regression"})
    public void userStartsBuildWithWireMockTest() {
        var checkedBuildQueueRequest = new CheckedBase<Build>(Specifications
                .mockSpec(), BUILD_QUEUE);

        var build = checkedBuildQueueRequest.create(testData.getBuild());

        softy.assertEquals(build.getState(), "finished");
        softy.assertEquals(build.getStatus(), "SUCCESS");
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }
}
