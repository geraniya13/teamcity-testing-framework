package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.spec.RequestSpecifications;
import com.example.teamcity.ui.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static io.qameta.allure.Allure.step;

public class BaseUiTest extends BaseTest {
    protected static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";

    @BeforeSuite(alwaysRun = true)
    public void setupUiTest() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = "http://%s".formatted(Config.getProperty("host"));
        Configuration.browserSize = Config.getProperty("browserSize");
        if (Config.getProperty("isRemote").equals("true")) {
            Configuration.remote = Config.getProperty("remote");
            Configuration.browserCapabilities.setCapability("selenoid:options",
                    Map.of("enableVNC", true,
                            "enableLog", true));
        }


        //не писать юай тесты с локальным браузером
        //после запуска на ремоут браузере потребуется перенастройка
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected void loginAs(User user) {
        // подготовка окружения
        step("Login as user via API", () -> {
            superUserCheckRequests.getRequest(Endpoint.USERS).create(user);
            LoginPage.openLoginPage().login(user);
        });

    }

    protected Project createProject(User user,NewProjectDescription newProjectDescription) {
        AtomicReference<Project> project = new AtomicReference<>(new Project());
        step("Create new project via API", () -> {
            project.set(new CheckedRequests(RequestSpecifications.authSpec(user)).<Project>getRequest(PROJECTS).create(newProjectDescription));
        });
        return project.get();
    }

}
