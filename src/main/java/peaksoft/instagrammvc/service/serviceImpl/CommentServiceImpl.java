package peaksoft.instagrammvc.service.serviceImpl;

import peaksoft.instagrammvc.entity.Comment;
import peaksoft.instagrammvc.entity.Like;
import peaksoft.instagrammvc.entity.Post;
import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.repository.CommentRepository;
import peaksoft.instagrammvc.repository.PostRepository;
import peaksoft.instagrammvc.repository.UserRepository;
import peaksoft.instagrammvc.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void saveComment(Long userId, Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Like like = new Like();

        comment.setLike(like);
        user.getComments().add(comment);

        comment.setUser(user);
        post.getComments().add(comment);
        comment.setPost(post);
        commentRepository.save(comment);
    }
    @Override
    public void deleteComment(Long comId) {
        commentRepository.deleteById(comId);
    }

    @Override
    @Transactional
    public void getCommentLike(Long userId, Long comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        List<Long> isLikes = comment.getLike().getComLikes();
        if (isLikes.contains(userId)){
            isLikes.remove(userId);
        }else {
            isLikes.add(userId);
        }
    }

}
