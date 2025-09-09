package com.example.teamcity.api;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.api.enums.Scope;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.dataprovider.ProjectDataProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.Scope.PROJECT;
import static com.example.teamcity.api.enums.TsRole.PROJECT_ADMIN;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static com.example.teamcity.api.generators.random.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Test(groups = {"Regression"})
public class NewProjectTest extends BaseApiTest {
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with minimal mandatory correct data", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithMinCorrectDataTest() {

        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        var createdProject = systemAdminCheckRequests.<Project>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());

        step("Check project was created", () -> softy.assertEquals(testData.getNewProjectDescription().getName(), createdProject.getName(), "Project name is not correct"));
    }

    // content type case will always fail since TeamCity does not throw any exception regarding unexpected content type for text
    @Test(description = "User should not be able to create project without mandatory header", groups = {"Negative", "CRUD"}, dataProvider = "project_headers_negative", dataProviderClass = ProjectDataProvider.class)
    public void userCreatesProjectWithoutHeaderTest(String value, String header, int status, String error) {
        var userCheckRequests = new UncheckedRequests(Specifications.defaultSpec().header(header, value));

        step("Create project without " + header + " header and check status " + status + "and error message: " + error, () -> userCheckRequests
                .<Project>getRequest(PROJECTS)
                .create(testData.getNewProjectDescription()))
                .then()
                .assertThat()
                .statusCode(status)
                .body(Matchers.containsString(error));
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with wrong value in auth header", groups = {"Negative", "CRUD"}, dataProvider = "project_invalid_auth_negative", dataProviderClass = ProjectDataProvider.class)
    public void systemAdminCreatesProjectWithWrongInfoInAuthHeaderTest(String userName, String userPassword, String encoded) {
        testData.getUser().setUsername(userName);
        testData.getUser().setPassword(userPassword);
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));
        var systemAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec((testData.getUser())).header("Authorization", encoded));
        step("Create project with wrong value auth header heck 401 status error message", () -> {
            systemAdminUncheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .body(Matchers.containsString("Incorrect username or password"));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with wrong content in content type header", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithWrongInfoInContentHeaderTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec((testData.getUser())).contentType(ContentType.HTML));
        step("Create project with wrong content type in content type header and check status 415 and error message", () ->
                systemAdminUncheckRequests.<Project>getRequest(PROJECTS).create("<html></html>")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                        .body(Matchers.containsString("Unsupported Media Type")
                        ));
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project without body", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithoutBodyTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        step("Create project without body and check 500 error and error message", () -> RestAssured.given()
                .spec(Specifications.authSpec(testData.getUser()))
                .when()
                .post(PROJECTS.getUrl()))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("Cannot read field \\\"name\\\" because \\\"descriptor\\\" is null"));
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with empty body", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithoutProperBodyTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        step("Create project with empty body and check 500 error and error message", () -> {
            new UncheckedRequests(Specifications.authSpec((testData.getUser()))).<Project>getRequest(PROJECTS).create("")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body(Matchers.containsString("Cannot read field \\\"name\\\" because \\\"descriptor\\\" is null"));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project without name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithoutNameTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        step("Create project without name and check 500 error and error message", () -> {
            given()
                    .spec(Specifications.authSpec(testData.getUser()))
                    .body("{\"id\": \"noName\",\"copyAllAssociatedSettings\": true}")
                    .when()
                    .post(PROJECTS.getUrl())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString("Project name cannot be empty."));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not able to create project without id field", groups = {"Negative", "CRUD"}, dataProvider = "project_absent_id_positive", dataProviderClass = ProjectDataProvider.class)
    public void systemAdminCreatesProjectWithoutIdTest(String name, String id) {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        step("Create project without mandatory id and check project created with id copied from name and adjusted", () -> {
            Project project = given()
                    .spec(Specifications.authSpec(testData.getUser()))
                    .body("{\"name\": \"" + name + "\",\"copyAllAssociatedSettings\": true}")
                    .when()
                    .post(PROJECTS.getUrl())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body(Matchers.containsString("\"id\":\"" + id + "\""))
                    .extract().as(Project.class);

            TestDataStorage.getStorage().addCreatedEntity(PROJECTS, project);
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with invalid name", groups = {"Negative", "CRUD"}, dataProvider = "project_invalid_name_negative", dataProviderClass = ProjectDataProvider.class)
    public void systemAdminCreatesProjectWithNameMalformationsTest(String name, int status, String error) {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with " + name + " name and check status " + status + " and error message " + error, () -> {
            testData.getNewProjectDescription().setName(name);
            systemAdminUncheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(status)
                    .body(Matchers.containsString(error));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with correct name", groups = {"Positive", "CRUD"}, dataProvider = "project_valid_name_positive", dataProviderClass = ProjectDataProvider.class)
    public void systemAdminCreatesProjectWithValidNameTest(String name) {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminUncheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with " + name, () -> {
            testData.getNewProjectDescription().setName(name);

            systemAdminUncheckRequests.getRequest(PROJECTS).create(testData.getNewProjectDescription());
        });

        step("Check project was created", () -> {
            Project createdProject = systemAdminUncheckRequests.<Project>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());
            softy.assertEquals(createdProject.getName(), name);
        });
    }

    // case with 81 letters fails due to contradiction with requirement
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with invalid id", groups = {"Negative", "CRUD"}, dataProvider = "project_invalid_id_negative", dataProviderClass = ProjectDataProvider.class)
    public void systemAdminCreatesProjectWithInvalidIdTest(String id, int status, String error) {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with " + id + " id and check status " + status + " and error message " + error, () -> {
            testData.getNewProjectDescription().setId(id);
            systemAdminUncheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(status)
                    .body(Matchers.containsString(error));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with unexistent project parent", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithUnexistentParentTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with unexistent project parent and check 404 status and error message", () -> {
            testData.getNewProjectDescription().setParentProject(testData.getProject());

            systemAdminUncheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(404)
                    .body(Matchers.containsString("Project cannot be found by external id '%s'".formatted(testData.getProject().getId())));
            ;
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with existent id", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithExistentIdTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        step("Create project with existent id and check status 400 and error message", () -> {
            testData.getNewProjectDescription().setName("Updated Name");
            new UncheckedRequests(Specifications.authSpec(testData.getUser())).<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString(String.format("Project ID \\\"%s\\\" is already used by another project.", testData.getNewProjectDescription().getId())));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to create project with existent name", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithExistentNameTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        step("Create project with existent id and check status 400 and error message", () -> {
            testData.getNewProjectDescription().setId("updatedId");
            new UncheckedRequests(Specifications.authSpec(testData.getUser())).<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString(String.format("Project with this name already exists: %s", testData.getNewProjectDescription().getName())));
        });
    }

    @WithUserRole(role = PROJECT_ADMIN)
    @Test(description = "Project Admin should be able to create project with granted project as parent", groups = {"Positive", "CRUD"})
    public void projectAdminCreatesSubproject() {
        testData.getUser().getRoles().getRole()
                .set(0, Scope.setProjectScope(testData.getUser().getRoles().getRole().get(0),
                        testData.getNewProjectDescription().getId()));
        var parentProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());

        step("Create project admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var projectAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        var childProject = generate(NewProjectDescription.class);

        step("Create project with granted project as parent", () -> {
            childProject.setParentProject(parentProject);
            projectAdminCheckRequests.<Project>getRequest(PROJECTS).create(childProject);
        });

        step("Check project was created with correct parent", () -> {
            Project createdProject = projectAdminCheckRequests.<Project>getRequest(PROJECTS).read(childProject.getId());
            softy.assertEquals(createdProject.getParentProjectId(), parentProject.getId());
        });
    }

    //per project permissions option should be enabled in team city config for this case
    @WithUserRole(role = PROJECT_ADMIN, scope = PROJECT)
    @Test(description = "Project Admin should not be able to create project with foreign project as parent", groups = {"Negative", "CRUD"})
    public void projectAdminCreatesSubprojectInForeignProject() {
        testData.getUser().getRoles().getRole()
                .set(0, Scope.setProjectScope(testData.getUser().getRoles().getRole().get(0),
                        testData.getNewProjectDescription().getId()));

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());

        step("Create project admin and foreign project", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var projectAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        var foreignProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        step("Create project with foreign project as parent and check 403 error and error message", () -> {
            var childProject = generate(NewProjectDescription.class);
            childProject.setParentProject(foreignProject);
            projectAdminUncheckRequests.<Project>getRequest(PROJECTS).create(childProject)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body(Matchers.containsString("You do not have \\\"Create subproject\\\" permission"));
        });
    }

    //per project permissions option should be enabled in team city config for this case
    @Test(description = "User should not be able to create project with root as parent", groups = {"Negative", "CRUD"}, dataProvider = "project_root_roles_negative", dataProviderClass = ProjectDataProvider.class)
    public void userCreatesProjectInRoot(Role role) {
        role = Scope.setProjectScope(role, testData.getNewProjectDescription().getId());
        Roles roles = Roles.builder().role(Arrays.asList(role)).build();
        testData.getUser().setRoles(roles);

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());

        step("Create user with role " + role.getRoleId(), () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var projectAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with root as parent and check 403 status and error message", () -> {
            projectAdminUncheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class))
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body(Matchers.containsString("You do not have \\\"Create subproject\\\" permission in project with internal id: _Root."));
        });
    }

    //per project permissions option should be enabled in team city config for this case
    @Test(description = "User should not be able to create subproject", groups = {"Negative", "CRUD"}, dataProvider = "project_creation_roles_negative", dataProviderClass = ProjectDataProvider.class)
    public void userCreatesSubproject(Role role) {
        role = Scope.setProjectScope(role, testData.getNewProjectDescription().getId());
        Roles roles = Roles.builder().role(Arrays.asList(role)).build();
        testData.getUser().setRoles(roles);

        Project parentProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());

        step("Create user with role " + role.getRoleId(), () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var projectAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with granted project as parent and check 403 status and error message", () -> {
            NewProjectDescription childProject = generate(NewProjectDescription.class);
            childProject.setParentProject(parentProject);
            projectAdminUncheckRequests.<Project>getRequest(PROJECTS).create(childProject)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body(Matchers.containsString("You do not have \\\"Create subproject\\\" permission"));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with different parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentParentAndSourceTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        Project project1 = systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        Project project2 = systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        Property property = Property.builder()
                .name("testProperty")
                .value("testValue")
                .build();

        project2.setParameters(Properties.builder().property(Arrays.asList(property)).build());
        systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(property, "/" + project2.getId() + "/parameters");

        step("Create project with different parent and source projects", () -> {
            testData.getNewProjectDescription().setParentProject(project1);
            testData.getNewProjectDescription().setSourceProject(project2);
            systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());
        });

        step("Check parent and source of created project", () -> {
            Project createdProject = systemAdminCheckRequests.<Project>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());

            softy.assertEquals(createdProject.getParentProject().getId(), project1.getId());
            softy.assertEquals(createdProject.getParameters().getProperty().get(0).getValue(), "testValue");
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to create project with same parent and source projects", groups = {"Positive", "CRUD"})
    public void systemAdminCreatesProjectWithSameParentAndSourceTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        Project parentProject = systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        testData.getNewProjectDescription().setParentProject(parentProject);
        testData.getNewProjectDescription().setSourceProject(parentProject);

        step("Create project with parent as source project", () -> systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        step("Check parent and source of created project", () -> {
            Project createdProject = systemAdminCheckRequests.<Project>getRequest(PROJECTS).read(testData.getNewProjectDescription().getId());

            softy.assertEquals(createdProject.getParentProject().getId(), parentProject.getId());
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should not be able to specify both source project and source locator", groups = {"Negative", "CRUD"})
    public void systemAdminCreatesProjectWithDifferentSourceLocatorAndSourceTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        Project project1 = systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        Project project2 = systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(generate(NewProjectDescription.class));

        step("Create project with 1 as source project and 2 as source locator and check 400 error and error message", () -> {
            testData.getNewProjectDescription().setSourceProject(project1);
            testData.getNewProjectDescription().setSourceProjectLocator("id:" + project2.getId());
            new UncheckedRequests(Specifications.authSpec(testData.getUser())).<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString("Both 'sourceProject' and 'sourceProjectLocator' are specified."));
        });
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "System Admin should be able to find existing project by name", groups = {"Positive", "CRUD"})
    public void systemAdminSearchesProjectByNameTest() {
        step("Create system admin", () -> superUserCheckRequests.getRequest(USERS).create(testData.getUser()));

        var systemAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project", () -> systemAdminCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription()));

        step("Search for created project by name and check project was successfully found", () -> {
            Project result = systemAdminCheckRequests.<Project>getRequest(PROJECTS).searchSingle(new String[]{"name", testData.getNewProjectDescription().getName()});
            softy.assertEquals(result.getId(), testData.getNewProjectDescription().getId());
        });


    }


}
