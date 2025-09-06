package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends BaseModel {
    @Random
    private String id;
    private String internalId;
    private String uuid;
    @Random
    private String name;

    private String parentProjectId;
    private String parentProjectInternalId;
    private String parentProjectName;

    private boolean archived;
    private boolean virtual;

    private String description;
    private String href;
    private String webUrl;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Properties parameters;

    //    private Links links;
    @Optional
    private Project parentProject;            // ссылка на родителя (рекурсивно)

    //    private StateField readOnlyUI;
    @Optional
    private BuildType defaultTemplate;
    private String locator;

    //    private BuildTypes buildTypes;
//    private BuildTypes templates;
//
//    private DeploymentDashboards deploymentDashboards;
                // см. ниже (count/href/List<PropertyDto>)
//    private VcsRoots vcsRoots;
//
//    private ProjectFeatures projectFeatures;
//    private Projects projects;
//    private CloudProfiles cloudProfiles;
//    private Projects ancestorProjects;


}
