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
import org.springframework.web.bind.annotation.RestController;

import com.blog.constants.AppApiNames;
import com.blog.dto.CommentDto;
import com.blog.entity.Comment;
import com.blog.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController implements AppApiNames {

	private CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long post_id,
			@RequestBody CommentDto requestCommentDto) {
		System.out.println(CREATE_COMMENT_API);
		CommentDto createdComment = commentService.createComment(post_id, requestCommentDto);
		ResponseEntity<CommentDto> responseCommentDto = new ResponseEntity<>(createdComment, HttpStatus.CREATED);

		return responseCommentDto;

	}

	@GetMapping("/post/{postId}/comment")
	public List<CommentDto> findAllCommentsByPostId(@PathVariable(name = "postId") Long postId) {
		System.out.println(GET_COMMENT_BY_POST_ID_API);
		List<CommentDto> responseComments = commentService.findByPostId(postId);

		return responseComments;

	}

	@GetMapping("/post/{postId}/comment/{id}")
	public ResponseEntity<CommentDto> findCommentByCommentId(@PathVariable(name = "postId") Long postId,
			@PathVariable(name = "id") Long id) {
		System.out.println(GET_COMMENT_BY_COMMENT_ID_API);
		CommentDto commentDto = commentService.findCommentByCommentId(postId, id);

		return new ResponseEntity<>(commentDto, HttpStatus.OK);

	}

	@PutMapping("/post/{postId}/comment/{commentId}")
	public ResponseEntity<CommentDto> updateCommentByCommentId(@PathVariable Long postId, @PathVariable Long commentId,
			@RequestBody CommentDto requestCommentDto) {
		System.out.println(UPDATE_COMMENT_BY_COMMENT_ID_API);
		CommentDto updatedDto = commentService.updateCommentByCommentId(postId, commentId, requestCommentDto);

		ResponseEntity<CommentDto> responsePostDto = new ResponseEntity<>(updatedDto, HttpStatus.OK);

		return responsePostDto;

	}

	@DeleteMapping("/post/{postId}/comment/{commentId}")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		System.out.println(DELETE_COMMENT_BY_COMMENT_ID);

		commentService.deleteCommentByCommentId(postId, commentId);

		return "Comment deleted sucessfully with comment Id: " + commentId;

	}

}
