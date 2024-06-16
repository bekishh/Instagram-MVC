package peaksoft.instagrammvc.service.serviceImpl;

import peaksoft.instagrammvc.entity.Follower;
import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.repository.FollowerRepository;
import peaksoft.instagrammvc.repository.UserRepository;
import peaksoft.instagrammvc.service.FollowerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    @Override
    public int getNumberOfSubscribers(Long userId) {
        Follower follower = followerRepository.findByUserId(userId);
        return follower.getSubscribers().isEmpty() ? 0 : follower.getSubscribers().size();
    }
    @Override
    public int getNumberOfSubscriptions(Long userId) {
        Follower follower = followerRepository.findByUserId(userId);
        return follower.getSubscriptions().isEmpty() ? 0 : follower.getSubscriptions().size();
    }
    @Transactional
    @Override
    public void addSubscriber(Long userId, Long subscriberId) {
        User user = userRepository.findById(userId).orElseThrow();
        User subscriber = userRepository.findById(subscriberId).orElseThrow();

        Follower userFollower = user.getFollower();
        Follower subscriberFollower = subscriber.getFollower();

        List<Long> userSubscriptions = userFollower.getSubscriptions();
        List<Long> subscriberSubscribers = subscriberFollower.getSubscribers();

        if (userSubscriptions.contains(subscriberId)) {
            userSubscriptions.remove(subscriberId);
            subscriberSubscribers.remove(userId);
        } else {
            userSubscriptions.add(subscriberId);
            subscriberSubscribers.add(userId);
        }
    }
}
