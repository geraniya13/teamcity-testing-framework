package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class BuildConfigurationElement extends BasePageElement{


    public BuildConfigurationElement(SelenideElement element) {
        super(element);
        this.name = find("span[class*='ProjectsTreeItem-module__name--IJ']");
        this.link = find("a");
    }
}
