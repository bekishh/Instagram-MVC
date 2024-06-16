package peaksoft.instagrammvc.service.serviceImpl;

import peaksoft.instagrammvc.entity.Image;
import peaksoft.instagrammvc.entity.Like;
import peaksoft.instagrammvc.entity.Post;
import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.exception.MyException;
import peaksoft.instagrammvc.repository.PostRepository;
import peaksoft.instagrammvc.repository.UserRepository;
import peaksoft.instagrammvc.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void createPost(Long userId, Post newPost){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with ID %d not found ",userId)));

        Like like = new Like();
        newPost.setLike(like);

        // Проходимся по всем изображениям в новом посте
        for (Image image : newPost.getImages()) {
            // Связываем изображение с постом
            image.setPost(newPost);
        }

        // Добавляем новый пост к пользователю
        user.getPosts().add(newPost);
        newPost.setUser(user);

        // Сохраняем пост в репозитории
        postRepository.save(newPost);
    }


    @Override
    public Post findById(Long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(() -> new MyException(String.format("Post with ID %d not found ",postId)));
        }catch (MyException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void updatePOst(Long postId, Post post) {
        Post findPost = findById(postId);

        findPost.setTitle(post.getTitle());
        findPost.setDescription(post.getDescription());

        postRepository.save(findPost);
    }

    @Override
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void getLikePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        List<Long> isLikes = post.getLike().getIsLikes();
        if (isLikes.contains(userId)) {
            isLikes.remove(userId);
        } else {
            isLikes.add(userId);
        }
    }
}
