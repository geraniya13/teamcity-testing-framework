package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.elements.BasePageElement;
import com.example.teamcity.ui.elements.BuildConfigurationElement;
import com.example.teamcity.ui.elements.ProjectElement;
import lombok.Getter;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

@Getter
public class ProjectsPage extends BasePage {
    private static final String PROJECTS_URL = "/favorite/projects";

    private final ElementsCollection projectsElements = $$(byAttribute("data-test-itemtype", "project")),
                               buildConfigurationsElements = $$(byAttribute("data-test-itemtype", "buildType"));



    private final SelenideElement header = $(".MainPanel-module__router--JB > div"),
            searchInput = $(byAttribute("data-test","sidebar-search"));

    // ElementsCollection -> List<ProjectElement>
    // UI element -> List<Object>
    // ElementCollection -> List<BasePageElement>

    public ProjectsPage() {
        header.shouldBe(visible, BASE_WAITING);
    }


    public static ProjectsPage openProjectsPage() {
        return open(PROJECTS_URL, ProjectsPage.class);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectsElements, ProjectElement::new);
    }

    public List<BuildConfigurationElement> getBuildConfigurations() {
        return generatePageElements(buildConfigurationsElements, BuildConfigurationElement::new);
    }

    public ProjectsPage searchItem (String item) {
        searchInput.click();
        searchInput.clear();
        searchInput.sendKeys(item);
        return this;
    }

    public <T extends BasePageElement> boolean isElementPresented(List<T> elements, String matchingText) {
        return elements.stream()
                .anyMatch(element -> element.getName().text().equals(matchingText));
    }

    public <T extends BasePageElement> T getElement (List<T> elements, String matchingText) {
        return elements.stream()
                .filter(element -> element.getName().text().equals(matchingText))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Element not found"));
    }
}
