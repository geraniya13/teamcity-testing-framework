package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.generators.random.TestDataGenerator;
import com.example.teamcity.api.models.AuthModules;
import com.example.teamcity.api.models.ServerAuthSettings;
import com.example.teamcity.api.requests.ServerAuthRequest;
import com.example.teamcity.api.spec.RequestSpecifications;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

import static com.example.teamcity.api.generators.random.TestDataGenerator.generate;

public class BaseApiTest extends BaseTest {
    private final ServerAuthRequest serverAuthRequest = new ServerAuthRequest(RequestSpecifications.superUserSpec());
    private AuthModules authModules;
    private boolean perProjectPermissions;

    @BeforeMethod(alwaysRun = true)
    public void apiBeforeMethod(Method method) {
        TestDataGenerator.applyUserRoles(testData, method);
    }

    @BeforeSuite(alwaysRun = true)
    public void setUpServerAuthSettings() {
        // Получаем текущие настройки perProjectPermissions
        perProjectPermissions = serverAuthRequest.read().getPerProjectPermissions();

        authModules = generate(AuthModules.class);
        // Обновляем значение perProjectPermissions на true
        serverAuthRequest.update(ServerAuthSettings.builder()
                .perProjectPermissions(true)
                .modules(authModules)
                .build());
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUpServerAuthSettings() {
        // Возвращаем настройке perProjectPermissions исходное значение
        serverAuthRequest.update(ServerAuthSettings.builder()
                .perProjectPermissions(perProjectPermissions)
                .modules(authModules)
                .build());
    }
}
