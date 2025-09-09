package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Scope {
    GLOBAL("g"),
    PROJECT("p");

    private final String scope;

    public static Role setProjectScope(Role role, String id) {
        return  Role.builder().roleId(role.getRoleId()).scope(PROJECT.getScope() + ":" + id).build();
    }
}
