package com.example.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class ProjectPage extends BasePage{
    private static final String PROJECT_URL = "/project/%s";

    public SelenideElement title = $("span[class*='ProjectPageHeader']");

    public static ProjectPage openProjectPage(String projectId) {
        return open(PROJECT_URL.formatted(projectId), ProjectPage.class);
    }
}
