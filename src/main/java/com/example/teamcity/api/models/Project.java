package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project extends BaseModel {
    @Random
    private String id;
    private String internalId;
    private String uuid;
    @Random
    private String name;
    private String parentProjectId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Optional
    private BuildTypes buildTypes;
    private String parentProjectInternalId;
    private String parentProjectName;
    private boolean archived;
    private boolean virtual;
    private String description;
    private String href;
    private String webUrl;
    private Properties parameters;
    @Optional
    private Project parentProject;
    @Optional
    private BuildType defaultTemplate;
    private String locator;
}
