package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.Scope.PROJECT;

@AllArgsConstructor
@Getter
public enum TsRole {
    SYSTEM_ADMIN("SYSTEM_ADMIN"),
    PROJECT_ADMIN("PROJECT_ADMIN"),
    PROJECT_DEVELOPER("PROJECT_DEVELOPER"),
    PROJECT_VIEWER("PROJECT_VIEWER");

    private final String role;

    public String globalScope() {
        return role + "/" + GLOBAL;
    }

    public String projectScope(Project project) {
        return role + "/" + PROJECT + ":" + project.getId();
    }


}
