package com.example.teamcity.api.models;

import lombok.Data;

@Data
public class TestData {
    private NewProjectDescription newProjectDescription;
    private Project project;
    private User user;
    private BuildType buildType;
    private Build build;
}
