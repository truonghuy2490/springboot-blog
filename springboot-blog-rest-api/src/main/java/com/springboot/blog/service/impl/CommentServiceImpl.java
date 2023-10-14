package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNoFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = mapEntity(commentDTO);

        // retrive post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNoFoundException("Post","id",postId)
        );
        // set post to comment
        comment.setPost(post);

        // comment entity to database
        Comment commentResponse = commentRepository.save(comment);
        return mapToDTO(commentResponse);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        // retrieve comment entity by post id
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(
                        comment -> mapToDTO(comment)
                ).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        // retrieve post entity by post id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNoFoundException("Post","id",postId)
        );
        // retrieve comment by commnet id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNoFoundException("Comment", "id", commentId)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not not belong to this post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentRequest) {
        // retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNoFoundException("Post", "id", postId)
        );
        // retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNoFoundException("Comment", "id", commentId)
        );
        if(!comment.getPost().getId().equals(post.getId())){
           throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment not belong to this post");
        }
            // update comment dto with database
            comment.setName(commentRequest.getName());
            comment.setEmail(commentRequest.getEmail());
            comment.setBody(commentRequest.getBody());

            Comment commentUpdate = commentRepository.save(comment);

        return mapToDTO(commentUpdate);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNoFoundException("Post", "id", postId)
        );
        // retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNoFoundException("Comment", "id", commentId)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment not belong to this post");
        }
        commentRepository.delete(comment);

    }

    private Comment mapEntity(CommentDTO commentDTO){
        Comment comments = new Comment();
        comments.setId(commentDTO.getId());
        comments.setName(commentDTO.getName());
        comments.setEmail(commentDTO.getEmail());
        comments.setBody(commentDTO.getBody());
        return comments;
    }
    private CommentDTO mapToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setEmail(comment.getEmail());
        commentDTO.setBody(comment.getBody());
        return commentDTO;
    }
}
