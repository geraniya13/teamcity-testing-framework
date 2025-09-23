package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu",
            NAME_INPUT_ERROR_MESSAGE = "Project name must not be empty";

    private SelenideElement projectNameInput = $("#projectName"),
            nameInputError = $("#error_projectName");

    public static CreateProjectPage openCreateProjectPage(String projectId) {
        return open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateProjectPage setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    public CreateProjectPage checkErrorMessage() {
        baseCheckErrorMessage(nameInputError, NAME_INPUT_ERROR_MESSAGE);
        return this;
    }
}
