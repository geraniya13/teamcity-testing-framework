package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CreateBuildConfigurationPage extends CreateBasePage {
    private static final String BUILD_SHOW_MODE = "createBuildTypeMenu",
            NAME_INPUT_ERROR_MESSAGE = "Build configuration name must not be empty";

    private SelenideElement nameInputError = $("#error_buildTypeName");

    public static CreateBuildConfigurationPage openCreateBuildConfigurationPage(String projectId) {
        return open(CREATE_URL.formatted(projectId, BUILD_SHOW_MODE), CreateBuildConfigurationPage.class);
    }

    public CreateBuildConfigurationPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateBuildConfigurationPage setupBuild(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    public CreateBuildConfigurationPage checkErrorMessage() {
        baseCheckErrorMessage(nameInputError, NAME_INPUT_ERROR_MESSAGE);
        return this;
    }
}
