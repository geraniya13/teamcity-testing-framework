package com.example.teamcity.ui;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {

    @Test(description = "User should be able to create project", groups = {"Positive"})
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    public void userCreatesProject(){
        loginAs(testData.getUser());

        // взаимодействие с юай
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Fix Project Name and Build Type Name values");
        step("Click `Proceed`");

        CreateProjectPage.openCreateProjectPage("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        // проверка состояния АПИ
        // корректность отправки данных с ЮАЙ на АПИ
        step("Check that all entities (project, buildType) were successfully created with correct data on API level");
        var createdProject = superUserCheckRequests.<Project>getRequest(PROJECTS).searchSingle(new String[]{"name", testData.getProject().getName()});
        softy.assertNotNull(createdProject);

        // проверка состояния ЮАЙ
        // корректность считывания данных от АПИ и отображение данных на ЮАЙ
        step("Check that project is visible on Projects Page (http://localhost:8111/favorite/projects)");
        ProjectPage.openProjectPage(createdProject.getId())
                .title.shouldHave(exactText(testData.getProject().getName()));


        var foundProjects = ProjectsPage.openProjectsPage()
                .getProjects().stream()
                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

        softy.assertTrue(foundProjects);
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        // подготовка окружения
        step("Login as user");
        step("Check number of projects");

        // взаимодействие с юай
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL");
        step("Click `Proceed`");
        step("Set Project Name value is empty");
        step("Click `Proceed`");

        // проверка состояния ЮАЙ
        // корректность считывания данных от АПИ и отображение данных на ЮАЙ
        step("Check that error appears `Project name must not be empty`");

        // проверка состояния АПИ
        // корректность отправки данных с ЮАЙ на АПИ
        step("Check that projects number wasn't changed");
    }
}
