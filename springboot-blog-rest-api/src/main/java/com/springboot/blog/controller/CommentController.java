package com.springboot.blog.controller;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable(value = "postId") Long postId,
            @Valid @RequestBody CommentDTO commentDTO
    )
    {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(
            @PathVariable(name = "postId") Long postId
    ){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId
    ){
        CommentDTO commentDTO = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PutMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment( @Valid
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ){
        CommentDTO updateComment = commentService.updateComment(postId, commentId, commentDTO);
        return new ResponseEntity<>(updateComment, HttpStatus.OK);
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId
    ){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment delete successfully", HttpStatus.OK);
    }
}
