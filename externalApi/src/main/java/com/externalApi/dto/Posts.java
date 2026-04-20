package com.externalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts {

        private Long id;
        private Long userId;
        private String title;
        private String body;
}
