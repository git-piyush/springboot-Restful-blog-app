package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.constants.AppApiNames;
import com.blog.dto.PostDto;
import com.blog.dto.PostResponseDto;
import com.blog.exception.ResourceNotFoundException;
import com.blog.service.PostService;
import com.blog.utils.AppConstants;

@RestController
@RequestMapping("/api/posts")
public class PostController implements AppApiNames {

	private PostService postService;

	@Autowired
	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}

	// create blog rest api
	// @RequestMapping(name = RequestMethod.POST) or
	@PostMapping
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto requestPostDto) {
		String apiName = CREATE_POST_API;
		System.out.println(apiName);
		PostDto createdPost = postService.createPost(requestPostDto);

		ResponseEntity<PostDto> responsePostDto = new ResponseEntity<>(createdPost, HttpStatus.CREATED);
		// System.out.println(postResponse);
		return responsePostDto;

	}

	@GetMapping("/getallposts")
	public PostResponseDto getAllPosts(
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		String apiName = GET_ALL_POSTS;
		System.out.println(apiName);
		PostResponseDto postResponseDto = postService.getAllPosts(pageSize, pageNo, sortBy, sortDir);
		return postResponseDto;

	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
		String apiName = GET_POST_BY_ID;
		System.out.println(apiName);

		// throw new ResourceNotFoundException("Post","id", id);

		PostDto fetchedPost = postService.getPostById(id);

		/*
		 * ResponseEntity<PostDto> responsePostDto = new ResponseEntity<>(fetchedPost,
		 * HttpStatus.OK);
		 * 
		 * return responsePostDto;
		 * 
		 * OR
		 */

		return ResponseEntity.ok(fetchedPost);

	}

	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto post, @PathVariable Long id) {
		String apiName = Update_Post;
		System.out.println(apiName);

		PostDto updatedPost = postService.updatePost(post, id);

		// ResponseEntity<PostDto> responsePostDto = new ResponseEntity<>(updatedPost,
		// HttpStatus.OK);

		// return responsePostDto;

		// or

		return ResponseEntity.ok(updatedPost);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id) {
		System.out.println(Delete_Post);
		String errorMessage;
		try {
			postService.deletePostById(id);
			errorMessage = "Post Deleted Sucessfully!!";
		} catch (Exception e) {
			throw new ResourceNotFoundException("Post", "Id", id);
			// errorMessage = "Exception in Post Deletion!!";
		}
		return new ResponseEntity<String>(errorMessage, HttpStatus.OK);

	}

}
