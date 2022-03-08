package com.blog.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.dto.CommentDto;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.exception.BlogApiException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;

	private PostRepository postRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	@Override
	public CommentDto createComment(long post_id, CommentDto commentDto) {

		Comment comment = mapToEntity(commentDto);

		Post post = postRepository.findById(post_id)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Id", post_id));

		comment.setPost(post);

		Comment newComment = commentRepository.save(comment);

		CommentDto responseCommentDto = mapToDto(newComment);

		return responseCommentDto;
	}

	public CommentDto mapToDto(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		commentDto.setName(comment.getName());
		commentDto.setEmail(comment.getEmail());
		commentDto.setBody(comment.getBody());

		return commentDto;
	}

	public Comment mapToEntity(CommentDto commentDto) {
		Comment comment = new Comment();
		comment.setId(commentDto.getId());
		comment.setName(commentDto.getName());
		comment.setEmail(commentDto.getEmail());
		comment.setBody(commentDto.getBody());

		return comment;
	}

	@Override
	public List<CommentDto> findByPostId(long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);

		List<CommentDto> commentsDto = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

		return commentsDto;

	}

	@Override
	public CommentDto findCommentByCommentId(Long postId, Long commentId) {

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));

		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment Does not belong to post");
		}

		return mapToDto(comment);
	}

	@Override
	public CommentDto updateCommentByCommentId(Long postId, Long commentId, CommentDto requestCommentDto) {

		// First find the post by postId, if not found throw throw respective exception
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "ID", postId));

		// second find the comment by commentId, if found throw respective exception
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));

		// match id from post and id from comment
		// If matched, then comment belongs to same post or throw respective exception
		if (!post.getId().equals(comment.getPost().getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment Does not belong to post");
		}

		// create comment from new comment dto
		//comment.setId(requestCommentDto.getId());
		comment.setName(requestCommentDto.getName());
		comment.setEmail(requestCommentDto.getEmail());
		comment.setBody(requestCommentDto.getBody());

		Comment updatedComment = commentRepository.save(comment);

		return mapToDto(updatedComment);
	}

	@Override
	public void deleteCommentByCommentId(Long postId, Long commentId) {
		
		Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
		
		Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "Id", commentId));
		
		if(!post.getId().equals(comment.getPost().getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment Does not belong to post");
		}
		
		commentRepository.deleteById(commentId);
		
	}

}
