package com.mylog.comment.service;

import com.mylog.comment.domain.Comment;
import com.mylog.comment.dto.CommentDataResponse;
import com.mylog.comment.dto.CommentEditRequest;
import com.mylog.comment.dto.CommentRemoveRequest;
import com.mylog.comment.dto.CommentSaveRequest;
import com.mylog.comment.exception.WrongCommentPasswordException;
import com.mylog.comment.repository.CommentRepository;
import com.mylog.post.domain.Post;
import com.mylog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<CommentDataResponse> AllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(c -> new CommentDataResponse(
                c.getId(), c.getWriter(), c.getContent(), c.getCreatedAt())).collect(Collectors.toList());
    }

    public Comment saveComment(CommentSaveRequest request) {
        Post post = postRepository.findById(request.getPostId()).orElse(null);

        Comment comment = new Comment(request.getWriter(), request.getPassword(), request.getContent(), post);
        return commentRepository.save(comment);
    }

    public void editComment(CommentEditRequest request) {
        Comment comment = commentRepository.findById(request.getId()).orElse(null);

        if (comment.getPassword().equals(request.getPassword()) == false) {
            throw new WrongCommentPasswordException();
        }

        comment.ediComment(request.getContent());
    }

    public void removeComment(CommentRemoveRequest request) {
        Comment comment = commentRepository.findById(request.getId()).orElse(null);

        if (comment.getPassword().equals(request.getPassword()) == false) {
            throw new WrongCommentPasswordException();
        }

        commentRepository.delete(comment);
    }
}
