package com.example.teamcity.api;

import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class NewProjectTest extends BaseApiTest {
    @Test(description = "System Admin should be able to create project with minimal correct data", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithMinCorrectDataTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        var createdProject = systemAdminCheckRequests.<NewProjectDescription>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());

        step("Check project was created", () -> softy.assertEquals(testData.getNewProjectDescription().getName(), createdProject.getName(), "Project name is not correct"));
    }


    @Test(description = "System Admin should not be able to create project without authorisation header", groups = {"Negative", "CRUD"}, dataProvider = "auth, content")
    public void systemAdminCreatesProjectWithoutHeaderTest(String header, String error, String message) {
        step("Create system admin");
        step("Create project without " + header + " header");
        step("Check " + error + " error and error message: " + message);
    }

    @Test(description = "System Admin should not be able to create project with wrong user in authorisation header", groups = {"Negative", "CRUD"}, dataProvider = "[user, auth, 401, mes], [password, auth, 401, mes], [content, content, 415, mes]")
    public void systemAdminCreatesProjectWithWrongInfoInHeaderTest(String value, String header, String error, String message) {
        step("Create system admin");
        step("Create project with wrong " + value + "in " + header + " header");
        step("Check " + error + " error and error message: " + message);
    }

    @Test(description = "System Admin should not be able to create project without body", groups = {"Negative", "CRUD"}, dataProvider = "[n, n, , ], [y, y, , ], [y, y,,id], [y,y,name,]]")
    public void systemAdminCreatesProjectWithoutProperBodyTest(String isBodyPresent, String isBodyEmpty, String name, String id) {
        step("Create system admin");
        step("Create project without empty body");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project without name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithoutNameTest() {
        step("Create system admin");
        step("Create project without name");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project without id", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithoutIdTest() {
        step("Create system admin");
        step("Create project without id");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with incorrect name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithIncorrectNameTest() {
        step("Create system admin");
        step("Create project with incorrect name");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with incorrect id", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithIncorrectIdTest() {
        step("Create system admin");
        step("Create project with incorrect id");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with body containing duplicated name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithDuplicatedNameTest() {
        step("Create system admin");
        step("Create project with body containing duplicated name");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with body containing duplicated id", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithDuplicatedIdTest() {
        step("Create system admin");
        step("Create project with body containing duplicated id");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should be able to create project with full and correct data", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithAllCorrectDataTest() {
        step("Create system admin");
        step("Create project with full and correct data");
        step("Check project was created");
        step("Check data of created project");
    }

    @Test(description = "System Admin should not be able to create project with unexistent project parent", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithUnexistentParentTest() {
        step("Create system admin");
        step("Create project with unexistent project parent");
        step("Check 400 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with existent project name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithExistentNameTest() {
        step("Create system admin");
        step("Create project with existent project name");
        step("Check 409 error and error message");
    }

    @Test(description = "System Admin should not be able to create project with existent project id", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithExistentIdTest() {
        step("Create system admin");
        step("Create project with existent project id");
        step("Check 409 error and error message");
    }

    @Test(description = "Project Admin should be able to create project with granted project as parent", groups = {"Positive", "CRUD"})
    public void projectAdminCreatesSubproject() {
        step("Create project admin");
        step("Create project with granted project as parent");
        step("Check project was created");
    }

    @Test(description = "Project Admin should not be able to create project with root as parent", groups = {"Negative", "CRUD"})
    public void projectAdminCreatesProjectInRoot() {
        step("Create project admin");
        step("Create project with root as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Project Admin should not be able to create project with foreign project as parent", groups = {"Negative", "CRUD"})
    public void projectAdminCreatesSubprojectInForeignProject() {
        step("Create project admin");
        step("Create project with foreign project as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Project developer should not be able to create project with granted project as parent", groups = {"Negative", "CRUD"})
    public void projectDeveloperCreatesSubproject() {
        step("Create project developer");
        step("Create project with granted project as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Project developer should not be able to create project with root as parent", groups = {"Negative", "CRUD"})
    public void projectDeveloperCreatesProjectInRoot() {
        step("Create project developer");
        step("Create project with root as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Project viewer should not be able to create project with granted project as parent", groups = {"Negative", "CRUD"})
    public void projectViewerCreatesSubproject() {
        step("Create project viewer");
        step("Create project with granted project as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Project viewer should not be able to create project with root as parent", groups = {"Negative", "CRUD"})
    public void projectViewerCreatesProjectInRoot() {
        step("Create project viewer");
        step("Create project with root as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "Agent manager should not be able to create project with root as parent", groups = {"Negative", "CRUD"})
    public void agentManagerCreatesProjectInRoot() {
        step("Create agent manager");
        step("Create project with root as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "System Admin should be able to create project with different parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentParentAndSourceTest() {
        step("Create system admin");
        step("Create parent project");
        step("Create source project with full data");
        step("Create project with different parent and source projects");
        step("Check project was created");
        step("Check parent and source of created project");
    }

    @Test(description = "System Admin should be able to create project with same parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithSameParentAndSourceTest() {
        step("Create system admin");
        step("Create parent project with full data");
        step("Create project with parent as source project");
        step("Check project was created");
        step("Check parent and source of created project");
    }

    @Test(description = "System Admin should not be able to create project with different source locator and source projects", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentSourceLocatorAndSourceTest() {
        step("Create system admin");
        step("Create 1 source project with full data");
        step("Create 2 source project with full data");
        step("Create project with 1 as source project and 2 as source locator");
        step("Check 400 error and error message");
    }


}
