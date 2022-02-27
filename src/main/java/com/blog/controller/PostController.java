package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.constants.AppConstants;
import com.blog.dto.PostDto;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController implements AppConstants {

	private PostService postService;

	@Autowired
	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}

	// create blog rest api
	// @RequestMapping(name = RequestMethod.POST) or
	@PostMapping
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
		String apiName = CREATE_POST_API;
		System.out.println(apiName);
		PostDto newPost = postService.createPost(postDto);

		ResponseEntity<PostDto> responsePostDto = new ResponseEntity<>(newPost, HttpStatus.CREATED);
		// System.out.println(postResponse);
		return responsePostDto;

	}

	@GetMapping("/getallposts")
	public List<PostDto> getAllPosts() {
		String apiName = GET_ALL_POSTS;
		System.out.println(apiName);
		return postService.getAllPosts();

	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
		String apiName = GET_POST_BY_ID;
		System.out.println(apiName);

		//throw new ResourceNotFoundException("Post","id", id);
		
		PostDto post = postService.getPostById(id);

		ResponseEntity<PostDto> responsePostDto = new ResponseEntity<>(post, HttpStatus.OK);

		return responsePostDto;
		

	}

}
