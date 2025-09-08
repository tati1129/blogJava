package org.workingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Integer id;
    private String postTitle;
    private String postDescription;
    private String userName;
    private String lastUpdate;
    private String status;
}
