package com.springboot.blog.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
public class PostDTO {
    private Long id;

    // title must not be null
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    // description should not be null or empty
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;

    // content should not be empty or null
    @NotEmpty
    private String content;
    private Set<CommentDTO> comments;

}
