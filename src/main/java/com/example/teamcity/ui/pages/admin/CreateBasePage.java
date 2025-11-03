package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;

public abstract class CreateBasePage extends BasePage {
    protected static final String CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=%s";


    protected SelenideElement urlInput = $("#url"),
    submitButton = $(byAttribute("value", "Proceed")),
    buildTypeNameInput = $("#buildTypeName"),
    connectionSuccessfulMessage = $(".connectionSuccessful");


    protected void baseCreateForm(String url) {
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, BASE_WAITING);
    }

    protected void baseCheckErrorMessage (SelenideElement locator, String errorMessageObject) {
        locator.shouldHave(exactText(errorMessageObject));
    }

}
