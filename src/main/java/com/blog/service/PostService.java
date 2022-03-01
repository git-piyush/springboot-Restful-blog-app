package com.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.dto.PostDto;
import com.blog.dto.PostResponseDto;

@Service
public interface PostService {

	PostDto createPost(PostDto postDto);
	
	PostResponseDto getAllPosts(int pageSize, int pageNo, String sortBy, String sortDir);

	PostDto getPostById(Long id);

	PostDto updatePost(PostDto post, Long id);

	void deletePostById(Long id);
	
}
