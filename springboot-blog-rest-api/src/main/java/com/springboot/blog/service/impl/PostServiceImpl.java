package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNoFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    @Override
    public PostDTO createPost(PostDTO postDTO) {

        // convert DTO to entity
        Post post = mapToEntity(postDTO);

        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostDTO postResponse = mapToDTO(newPost);

        return postResponse;
    }
    @Override
    public PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // CREATE PAGEABLE INSTANCE
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);


        // SAVE TO REPO
        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setTotalPage(posts.getTotalPages());
        postResponse.setTotalElement(posts.getTotalElements());
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }
    // convert entity to DTO
    private PostDTO mapToDTO(Post post){
//        PostDTO postDTO = new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setTitle(post.getTitle());
//        postDTO.setDescription(post.getDescription());
//        postDTO.setContent(post.getContent());
        return modelMapper.map(post, PostDTO.class);
    }
    private Post mapToEntity(PostDTO postDTO){
//        Post post = new Post();
//        post.setId(postDTO.getId());
//        post.setTitle(postDTO.getTitle());
//        post.setDescription(postDTO.getDescription());
//        post.setContent(postDTO.getContent());
        return modelMapper.map(postDTO, Post.class);
    }
    @Override
    public PostDTO getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Post", "id", id));

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post updatePost = postRepository.save(post);

        return mapToDTO(updatePost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Post", "id", id));
        postRepository.save(post);
    }
}
