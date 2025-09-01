package com.example.teamcity.api.generators.factory;

import com.example.teamcity.api.enums.TsRole;
import com.example.teamcity.api.enums.Scope;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.teamcity.api.enums.Scope.GLOBAL;
import static com.example.teamcity.api.enums.Scope.PROJECT;

public final class UserFactory {

    private UserFactory() {
    }

    /**
     * Вернуть копию user с добавленной ГЛОБАЛЬНОЙ ролью
     */
    public static User withGlobalRole(User user, TsRole tsRole) {
        return upsertRole(user, tsRole, GLOBAL, null);
    }

    /**
     * Вернуть копию user с добавленной ПРОЕКТНОЙ ролью
     */
    public static User withProjectRole(User user, TsRole tsRole, String projectId) {
        return upsertRole(user, tsRole, PROJECT, projectId);
    }

    // ------------------ helpers ------------------
    private static User upsertRole(User user, TsRole tsRole, Scope scope, String projectId) {
        Role newRole = Role.builder()
                .roleId(tsRole.getRole())                          // << из твоего enum'а
                .scope(scope == Scope.GLOBAL ? "g" : "p:" + projectId) // << "g" или "p:<id>"
                .build();

        List<Role> current = (user.getRoles() != null && user.getRoles().getRole() != null)
                ? user.getRoles().getRole()
                : List.of();
        List<Role> editable = new ArrayList<>(current);            // делаем mutаble копию

        // если в DTO лежит единственная "пустая" роль — заменяем её
        if (editable.size() == 1 && editable.get(0) != null && editable.get(0).getRoleId() == null) {
            editable.clear();
            editable.add(newRole);
        } else {
            boolean exists = editable.stream().anyMatch(r ->
                    r != null &&
                            Objects.equals(r.getRoleId(), newRole.getRoleId()) &&
                            Objects.equals(r.getScope(),  newRole.getScope()));
            if (!exists) editable.add(newRole);
        }

        return user.toBuilder()
                .roles(Roles.builder().role(editable).build())
                .build();
    }
//    private static void withRole(User user, TsRole tsRole, Scope scope, String projectId) {
//        var userRole = Role.builder()
//                .roleId(tsRole.getRole())
//                .scope(scope == GLOBAL ? scope.getScope() : scope.getScope() + ":" + projectId)
//                .build();
//
//        List<String> s = new ArrayList<>();
//
//        if (user.getRoles().getRole().size() == 1 && user.getRoles().getRole().get(0).getRoleId() == null) {
//            user.getRoles().getRole().set(0, userRole);
//        } else {
//            var merged = merge(user.getRoles(), userRole);
//            user.setRoles(Roles.builder().role(merged).build());
//        }
//    }

    private static List<Role> merge(Roles existing, Role toAdd) {
        var result = new ArrayList<Role>();
        if (existing != null && existing.getRole() != null) {
            result.addAll(existing.getRole());
        }
        var dup = result.stream().anyMatch(r ->
                Objects.equals(r.getRoleId(), toAdd.getRoleId()) &&
                        Objects.equals(r.getScope(), toAdd.getScope()));
        if (!dup) result.add(toAdd);
        return result;
    }
}