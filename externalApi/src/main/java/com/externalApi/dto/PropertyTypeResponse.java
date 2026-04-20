package com.externalApi.dto;

import lombok.Data;
import java.util.List;

@Data
public class PropertyTypeResponse {
    private List<PropertyType> result;
}