package com.example.teamcity.dataprovider;


import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.Role;
import org.testng.annotations.DataProvider;

import java.util.Base64;

import static com.example.teamcity.api.enums.TsRole.*;


public class ProjectDataProvider {
    @DataProvider(name = "project_headers_negative")
    public static Object[][] headersNegative() {
        return new Object[][]{
                {"Basic " + Base64.getEncoder().encodeToString((":" + Config.getProperty("superUserToken")).getBytes()), "Authorization", 415, "Expected application/json"},
                {"application/json", "Content-Type", 401, "Authentication required"}
        };
    }

    @DataProvider(name = "project_invalid_auth_negative")
    public static Object[][] invalidAuthNegative() {
        return new Object[][]{
                {"userName", "userPassword", "Basic " + Base64.getEncoder().encodeToString("userInvalidName:userPassword".getBytes())},
                {"userName", "userPassword", "Basic " + Base64.getEncoder().encodeToString("userName:userInvalidPassword".getBytes())}
        };
    }

    @DataProvider(name = "project_absent_id_positive")
    public static Object[][] idFieldPositive() {
        return new Object[][]{
                {"noId", "NoId"},
                {"1noId", "id1noId"},
                {"no Id", "NoId"},
                {"no&Id", "NoId"}
        };
    }

    @DataProvider(name = "project_invalid_name_negative")
    public static Object[][] invalidNameNegative() {
        return new Object[][]{
                {"", 400, "Project name cannot be empty."},
                {" ", 500, "Given project name is empty."},
                {"   ", 500, "Given project name is empty."},
                {"\r\n", 500, "Given project name is empty."}
        };
    }

    @DataProvider(name = "project_valid_name_positive")
    public static Object[] validNamePositive() {
        return new Object[]{
                "0",
                "gÜltig",
                "Имя",
                "01.01.1970",
                "DELETE * FROM USERS;",
                "{\"name\": \"value\"}",
                "風花雪月龍虎山川東京物語心夢光影",
                "*'`|@/\\,;_$:&<>^?\"",
                "First\\r\\nSecond",
                "✓✔✖",
                "\\u0001\\u0002\\u0003",
                "test ".repeat(256)
        };
    }

    @DataProvider(name = "project_invalid_id_negative")
    public static Object[][] bodyNegative() {
        return new Object[][]{
                {"123", 500, "starts with non-letter character '1'"},
                {"_test", 500, "starts with non-letter character '_'"},
                {"t1".repeat(40) + "t", 500, "Maximum characters number was exceeded"},
                {"", 500, "Project ID must not be empty."},
                {"  ", 500, "Project ID must not be empty."},
                {"\r\n", 500, "Project ID must not be empty."},
                {"T*1", 500, "Project ID \\\"T*1\\\" is invalid: contains unsupported character '*'."},
                {"T(1", 500, "Project ID \\\"T(1\\\" is invalid: contains unsupported character '('."},
                {"T-1", 500, "Project ID \\\"T-1\\\" is invalid: contains unsupported character '-'."},
                {"T 1", 500, "Project ID \\\"T 1\\\" is invalid: contains unsupported character ' '."}
        };
    }

    @DataProvider(name = "project_root_roles_negative")
    public static Object[] rootRolesNegative() {
        return new Object[]{
                Role.builder().roleId(PROJECT_ADMIN.getRole()).build(),
                Role.builder().roleId(PROJECT_DEVELOPER.getRole()).build(),
                Role.builder().roleId(PROJECT_VIEWER.getRole()).build(),
                Role.builder().roleId(AGENT_MANAGER.getRole()).build()
        };
    }

    @DataProvider(name = "project_creation_roles_negative")
    public static Object[] projectCreationRolesNegative() {
        return new Object[]{
                Role.builder().roleId(PROJECT_DEVELOPER.getRole()).build(),
                Role.builder().roleId(PROJECT_VIEWER.getRole()).build(),
                Role.builder().roleId(AGENT_MANAGER.getRole()).build()
        };
    }
}
