package com.example.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BuildConfigurationPage extends BasePage {
    private static final String BUILD_CONFIGURATION_URL = "/buildConfiguration/%s";

    public SelenideElement title = $("h1[class*='BuildTypePageHeader'] > span:nth-child(2)");

    public static BuildConfigurationPage openBuildConfigurationPage (String buildConfigurationId) {
        return open(BUILD_CONFIGURATION_URL.formatted(buildConfigurationId), BuildConfigurationPage.class);
    }
}
