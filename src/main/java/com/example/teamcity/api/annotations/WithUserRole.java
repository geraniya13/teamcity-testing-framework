package com.example.teamcity.api.annotations;

import com.example.teamcity.api.enums.TsRole;
import com.example.teamcity.api.enums.Scope;

import java.lang.annotation.*;

import static com.example.teamcity.api.enums.Scope.GLOBAL;

@Repeatable(WithUserRoles.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WithUserRole {
    TsRole role();
    Scope scope() default GLOBAL; // G или P
//    String projectId() default "";                 // обязателен при scope=P
}
