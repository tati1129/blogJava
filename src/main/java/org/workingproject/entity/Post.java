package org.workingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "postTitle")
    private String postTitle;
    private String postDescription;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    private LocalDate createDate;
    private LocalDate lastUpdate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;
}
