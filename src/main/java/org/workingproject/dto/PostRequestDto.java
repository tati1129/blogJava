package org.workingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private String postTitle;
    private String postDescription;
    private Integer userId;
    private String lastUpdate;
}
