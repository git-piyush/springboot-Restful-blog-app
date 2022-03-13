package com.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	/*
	 * 
	 * Rules to define Query methods.
	 * Rule 1 – The name of the query method must start with findBy or getBy  
	 * or queryBy or countBy or readBy prefix. The findBy is mostly used by the developer.
	 * 
	 * 
	 */

	List<Comment> findByPostId(long postId);
	
}
