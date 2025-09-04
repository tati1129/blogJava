package org.workingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Integer id;
    private String postTitle;
    private String postDescription;
    private User user;
    private LocalDate createDate;
    private LocalDate lastUpdate;
    private PostStatus status;
}
