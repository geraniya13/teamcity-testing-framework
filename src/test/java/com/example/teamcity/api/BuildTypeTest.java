package com.example.teamcity.api;

import com.example.teamcity.api.annotations.WithUserRole;
import com.example.teamcity.api.enums.Scope;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.TsRole.PROJECT_ADMIN;
import static com.example.teamcity.api.enums.TsRole.SYSTEM_ADMIN;
import static com.example.teamcity.api.generators.random.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {
    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());

        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @WithUserRole(role = SYSTEM_ADMIN, scope = GLOBAL)
    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<NewProjectDescription>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES).create(buildTypeWithSameId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \\\"%s\\\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
    }

    @WithUserRole(role = PROJECT_ADMIN)
    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        step("Create user", () -> {
            Role role = Scope.setProjectScope(testData.getUser().getRoles().getRole().get(0), testData.getProject().getId());
            Roles roles = Roles.builder().role(Arrays.asList(role)).build();
            testData.getUser().setRoles(roles);

            step("Create project", () -> superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject()));

            superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        });

        step("Create buildType for project by user (PROJECT_ADMIN) and check buildType was created successfully", () -> {
            var projectAdminCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
            projectAdminCheckRequests.<BuildType>getRequest(BUILD_TYPES).create(testData.getBuildType());
            var createdBuildType = projectAdminCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
            softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName());
        });

    }

    @WithUserRole(role = PROJECT_ADMIN)
    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        Role role = Scope.setProjectScope(testData.getUser().getRoles().getRole().get(0), testData.getNewProjectDescription().getId());
        Roles roles = Roles.builder().role(Arrays.asList(role)).build();
        testData.getUser().setRoles(roles);

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getNewProjectDescription());

        step("Create project admin and another project", () -> {
            superUserCheckRequests.getRequest(USERS).create(testData.getUser());
            superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        });

        var projectAdminUncheckRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create buildType for foreign project by user and check buildType was not created with forbidden code", () -> {
            projectAdminUncheckRequests.<BuildType>getRequest(BUILD_TYPES).create(testData.getBuildType())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body(Matchers.containsString("You do not have enough permissions to edit project with id: " + testData.getProject().getId()));
        });
    }
}
