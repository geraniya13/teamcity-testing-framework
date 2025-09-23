package com.example.teamcity.ui;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.ui.pages.ProjectsPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static com.example.teamcity.ui.pages.ProjectsPage.openProjectsPage;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class SearchTest extends BaseUiTest {

    @Test(description = "User should be able to find project by name", groups = {"Positive"})
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    public void userFindsProjectByName() {
        loginAs(testData.getUser());
        var createdProject = createProject(testData.getUser(), testData.getNewProjectDescription());

        // взаимодействие с юай
        step("Open `Projects` Page (http://localhost:8111/favorite/projects)");
        ProjectsPage projectsPage = openProjectsPage();

        step("Insert project name into search input");

        projectsPage.searchItem(createdProject.getName());

        // проверка состояния ЮАЙ
        step("Check project is displayed in project tree");

        var foundProjects = projectsPage.getProjects();

        softy.assertTrue(projectsPage.isElementPresented(foundProjects, createdProject.getName()));

        step("Select project in project tree");

        projectsPage.getElement(foundProjects, createdProject.getName()).goToProject();

        // проверка состояния ЮАЙ
        step("Check found project name matches expected project name");

        projectsPage.getHeader().shouldHave(text(createdProject.getName()));
    }
}
