package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Scope {
    GLOBAL("g"),
    PROJECT("p");

    private final String scope;
}
