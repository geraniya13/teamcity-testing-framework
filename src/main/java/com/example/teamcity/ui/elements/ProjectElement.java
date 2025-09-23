package com.example.teamcity.ui.elements;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.ProjectsPage;
import lombok.Getter;

@Getter
public class ProjectElement extends BasePageElement {
    private SelenideElement button;

    public ProjectElement(SelenideElement element) {
        super(element);
        this.name = find("span[class*='ProjectsTreeItem-module__name--IJ']");
        this.link = find("a");
        this.button = find("button");
    }

    public ProjectsPage goToProject() {
        this.getLink().click();
        return Selenide.page(ProjectsPage.class);
    }
}
