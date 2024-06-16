package peaksoft.instagrammvc.service;

import peaksoft.instagrammvc.entity.Comment;

public interface CommentService {
    void saveComment(Long userId, Long postId, Comment comment);

    void deleteComment(Long comId);

    void getCommentLike(Long userId, Long comId);
}
