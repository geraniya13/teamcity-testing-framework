package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
// @JsonInclude(JsonInclude.Include.CUSTOM)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties extends BaseModel {
    @Builder.Default
    private Integer count = 0;
    private String href;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Property> property;
}
