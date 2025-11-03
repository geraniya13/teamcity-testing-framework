package com.example.teamcity.ui;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.BuildConfigurationPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateBuildConfigurationPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateBuildTest extends BaseUiTest {
    @Test(description = "User should be able to create build configuration", groups = {"Positive"})
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    public void userCreatesBuildConfiguration(){
        step("Log in to team city and create project via api");
        loginAs(testData.getUser());
        var createdProject = createProject(testData.getUser(), testData.getNewProjectDescription());

        // взаимодействие с юай
        step("Open `Create Build Configuration` (http://localhost:8111/admin/createObjectMenu.html) -> Send build repository URL -> Click `Proceed` -> Fix Build Type Name value -> Click `Proceed`");

        CreateBuildConfigurationPage.openCreateBuildConfigurationPage(createdProject.getId())
                        .createForm(REPO_URL)
                        .setupBuild(testData.getBuildType().getName());

        // проверка состояния АПИ
        // корректность отправки данных с ЮАЙ на АПИ
        step("Check that buildType entities was successfully created with correct data on API level");
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES).searchSingle(new String[]{"name", testData.getBuildType().getName()});
        softy.assertNotNull(createdBuildType);

        // проверка состояния ЮАЙ
        // корректность считывания данных от АПИ и отображение данных на ЮАЙ
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)");
        BuildConfigurationPage.openBuildConfigurationPage(createdBuildType.getId())
                .title.shouldHave(exactText(testData.getBuildType().getName()));

        var foundBuildTypes = ProjectsPage.openProjectsPage()
                .getBuildConfigurations().stream()
                .anyMatch(buildConfiguration -> buildConfiguration.getName().text().equals(testData.getBuildType().getName()));

        softy.assertTrue(foundBuildTypes);
    }


    @Test(description = "User should not be able to create build configuration without name", groups = {"Negative"})
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    public void userCreatesProjectWithoutName() {
        loginAs(testData.getUser());
        var createdProject = createProject(testData.getUser(), testData.getNewProjectDescription());
        var initBuildConfigurationsCount = superUserCheckRequests.<Project>getRequest(PROJECTS).read(createdProject.getId()).getBuildTypes().getCount();

        // взаимодействие с юай
        // проверка состояния ЮАЙ
        // корректность считывания данных от АПИ и отображение данных на ЮАЙ
        step("Open `Create Build Configuration` (http://localhost:8111/admin/createObjectMenu.html) -> Send build repository URL -> Click `Proceed` -> Set Build Type Name value to null -> Click `Proceed` -> Check that error appears `Build configuration name must not be empty`");

        CreateBuildConfigurationPage.openCreateBuildConfigurationPage(createdProject.getId())
                .createForm(REPO_URL)
                .setupBuild("")
                .checkErrorMessage();

        // проверка состояния АПИ
        // корректность отправки данных с ЮАЙ на АПИ
        step("Check that build configurations number wasn't changed");

        var resultBuildConfigurationsCount = superUserCheckRequests.<Project>getRequest(PROJECTS).read(createdProject.getId()).getBuildTypes().getCount();
        softy.assertEquals(resultBuildConfigurationsCount, initBuildConfigurationsCount);
    }
}
