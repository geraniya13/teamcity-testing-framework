package com.example.teamcity.api;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.Scope.PROJECT;
import static com.example.teamcity.api.enums.TsRole.PROJECT_ADMIN;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static com.example.teamcity.api.generators.random.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class NewProjectTest extends BaseApiTest {
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with minimal correct data", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithMinCorrectDataTest() {

        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        var createdProject = systemAdminCheckRequests.<NewProjectDescription>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());

        step("Check project was created", () -> softy.assertEquals(testData.getNewProjectDescription().getName(), createdProject.getName(), "Project name is not correct"));
    }


    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project without {header} header", groups = {"Negative", "CRUD"}, dataProvider = "auth, content")
    public void systemAdminCreatesProjectWithoutHeaderTest(String header, String error, String message) {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project without " + header + " header", () -> systemAdminCheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getNewProjectDescription()));
        step("Check " + error + " error and error message: " + message);
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with wrong {value} in {header} header", groups = {"Negative", "CRUD"}, dataProvider = "[user, auth, 401, mes], [password, auth, 401, mes], [content, content, 415, mes]")
    public void systemAdminCreatesProjectWithWrongInfoInHeaderTest(String value, String header, String error, String message) {
        step("Create system admin");
        step("Create project with wrong " + value + "in " + header + " header");
        step("Check " + error + " error and error message: " + message);
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project without presence of body with mandatory values", groups = {"Negative", "CRUD"}, dataProvider = "[n, n, , ], [y, y, , ], [y, y,,id], [y,y,name,]]")
    public void systemAdminCreatesProjectWithoutProperBodyTest(String isBodyPresent, String isBodyEmpty, String name, String id, String valueAbsence) {
        step("Create system admin");
        step("Create project with " + valueAbsence + " absence");
        step("Check 400 error and error message");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with {value} {malformation} ", groups = {"Negative", "CRUD"}, dataProvider = "[name, long], [id, long], [name, duplication], [id, duplication], [name, int], [id, int]....")
    public void systemAdminCreatesProjectWithMandatoryFieldsMalformationsTest(String value, String malformation) {
        step("Create system admin");
        step("Create project with " + value + malformation);
        step("Check 400 error and error message");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with full and correct data", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithAllCorrectDataTest() {
        step("Create system admin");
        step("Create project with full and correct data");
        step("Check project was created");
        step("Check data of created project");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with unexistent project parent", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithUnexistentParentTest() {
        step("Create system admin");
        step("Create project with unexistent project parent");
        step("Check 400 error and error message");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with existent {value}", groups = {"Negative", "CRUD"}, dataProvider = "name, id")
    public void systemAdminCreatesProjectWithExistentNameTest(String value) {
        step("Create system admin");
        step("Create project with existent "  + value);
        step("Check 409 error and error message");
    }

    @WithUserRole(role = PROJECT_ADMIN, scope = PROJECT)
    @Test(description = "Project Admin should be able to create project with granted project as parent", groups = {"Positive", "CRUD"})
    public void projectAdminCreatesSubproject() {
        step("Create project admin");
        step("Create project with granted project as parent");
        step("Check project was created");
    }

    @WithUserRole(role = PROJECT_ADMIN, scope = PROJECT)
    @Test(description = "Project Admin should not be able to create project with foreign project as parent", groups = {"Negative", "CRUD"})
    public void projectAdminCreatesSubprojectInForeignProject() {
        step("Create project admin");
        step("Create foreign project");
        step("Create project with foreign project as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "{User} should not be able to create project with root as parent", groups = {"Negative", "CRUD"}, dataProvider = "project admin, Project developer, Project viewer, Agent manager")
    public void userCreatesProjectInRoot(String user) {
        step("Create " + user);
        step("Create project with root as parent");
        step("Check 403 error and error message");
    }

    @Test(description = "{User} should not be able to create project with granted project as parent", groups = {"Negative", "CRUD"}, dataProvider = "project admin, Project developer, Project viewer")
    public void userCreatesSubproject(String user) {
        step("Create " + user);
        step("Create project with granted project as parent");
        step("Check 403 error and error message");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with different parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentParentAndSourceTest() {
        step("Create system admin");
        step("Create parent project");
        step("Create source project with full data");
        step("Create project with different parent and source projects");
        step("Check project was created");
        step("Check parent and source of created project");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with same parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithSameParentAndSourceTest() {
        step("Create system admin");
        step("Create parent project with full data");
        step("Create project with parent as source project");
        step("Check project was created");
        step("Check parent and source of created project");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with different source locator and source projects", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentSourceLocatorAndSourceTest() {
        step("Create system admin");
        step("Create 1 source project with full data");
        step("Create 2 source project with full data");
        step("Create project with 1 as source project and 2 as source locator");
        step("Check 400 error and error message");
    }


}
