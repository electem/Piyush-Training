package com.externalApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PropertyType {

    private Integer id;
    private String cultSwitchId;
    private Integer bookingDotComId;
    private String name;
    private String notes;
}