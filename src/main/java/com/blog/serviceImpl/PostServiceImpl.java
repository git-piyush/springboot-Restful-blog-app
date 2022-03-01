package com.blog.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.dto.PostDto;
import com.blog.dto.PostResponseDto;
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
	public PostResponseDto getAllPosts(int pageSize, int pageNo, String sortBy, String sortDir) {

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

		// Pageable page = PageRequest.of(pageNo, pageSize); //if sorting not required

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable page = PageRequest.of(pageNo, pageSize, sort);

		Page<Post> posts = postRepository.findAll(page);

		List<Post> respectivePosts = posts.getContent();

		List<PostDto> allPostsResponse = respectivePosts.stream().map(post -> mapToDto(post))
				.collect(Collectors.toList());

		PostResponseDto postResponseDto = new PostResponseDto();

		postResponseDto.setPostDto(allPostsResponse);
		postResponseDto.setPageNo(posts.getNumber());
		postResponseDto.setPageSize(posts.getSize());
		postResponseDto.setTotalPages(posts.getTotalPages());
		postResponseDto.setTotalElement(posts.getTotalElements());
		postResponseDto.setLast(posts.isLast());

		return postResponseDto;
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

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

		postRepository.deleteById(id);

	}

}
