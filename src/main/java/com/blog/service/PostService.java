package com.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.dto.PostDto;

@Service
public interface PostService {

	PostDto createPost(PostDto postDto);
	
	List<PostDto> getAllPosts();

	PostDto getPostById(Long id);
	
}
