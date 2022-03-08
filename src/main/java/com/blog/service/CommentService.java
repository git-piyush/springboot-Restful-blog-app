package com.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.dto.CommentDto;


@Service
public interface CommentService {

	CommentDto createComment(long post_id, CommentDto commentDto);
	
	List<CommentDto> findByPostId(long postId);
	
	CommentDto findCommentByCommentId(Long postId, Long commentId);
	
	CommentDto updateCommentByCommentId(Long postId, Long commentId, CommentDto requestCommentDto);

	void deleteCommentByCommentId(Long postId, Long commentId);
	
}
