package com.blog.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.dto.PostDto;
import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;

	@Autowired
	public PostServiceImpl(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}

	@Override
	public PostDto createPost(PostDto postDto) {

		// create dto to entity
		Post newPost = mapToEntity(postDto);
		Post createdPost = null;
		try {
			createdPost = postRepository.save(newPost);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("some error");
		}

		// convert entity to dto

		PostDto postResponse = mapToDto(createdPost);

		return postResponse;
	}

	private PostDto mapToDto(Post createdPost) {
		PostDto postResponse = new PostDto();

		postResponse.setId(createdPost.getId());
		postResponse.setTitle(createdPost.getTitle());
		postResponse.setDescription(createdPost.getDescription());
		postResponse.setContent(createdPost.getContent());
		return postResponse;
	}

	private Post mapToEntity(PostDto postDto) {
		Post newPost = new Post();
		newPost.setId(postDto.getId());
		newPost.setTitle(postDto.getTitle());
		newPost.setDescription(postDto.getDescription());
		newPost.setContent(postDto.getContent());
		return newPost;
	}

	@Override
	public List<PostDto> getAllPosts() {

		/*
		 * List<PostDto> allPostsDto=new ArrayList<PostDto>(); List<Post> allPosts =
		 * postRepository.findAll(); for(Post post : allPosts) {
		 * 
		 * System.out.println(post); System.out.println(mapToEntity(post));
		 * allPostsDto.add(mapToEntity(post)); } return allPostsDto;
		 * 
		 * OR
		 */
		// java streams implementation
		List<Post> allPosts = postRepository.findAll();

		List<PostDto> allPostsResponse = allPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

		return allPostsResponse;
	}

	@Override
	public PostDto getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
		PostDto responsePostDto = mapToDto(post);
		return responsePostDto;
	}

	@Override
	public PostDto updatePost(PostDto post, Long id) {
		PostDto currentPost = getPostById(id);
		// feed the updated data into current post
		currentPost.setTitle(post.getTitle());
		currentPost.setDescription(post.getDescription());
		currentPost.setContent(post.getContent());

		// convert DTO to entity and save it
		Post updatedPost = postRepository.save(mapToEntity(currentPost));

		return mapToDto(updatedPost);
	}

	@Override
	public void deletePostById(Long id) {
		
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","Id", id));
		
		postRepository.deleteById(id);
		
	}

}
