package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewProjectDescription extends BaseModel {
    @Random
    private String id;
    @Random
    private String name;
    @Builder.Default
    private boolean copyAllAssociatedSettings = true;
    private Properties projectsIdsMap;
    private Properties buildTypesIdsMap;
    private Properties vcsRootsIdsMap;
    private String sourceProjectLocator;
//    private Project sourceProject;
//    private Project parentProject;
}
