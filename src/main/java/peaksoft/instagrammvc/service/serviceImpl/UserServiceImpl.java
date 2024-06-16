package peaksoft.instagrammvc.service.serviceImpl;

import peaksoft.instagrammvc.entity.Follower;
import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.entity.UserInfo;
import peaksoft.instagrammvc.exception.MyException;
import peaksoft.instagrammvc.repository.UserRepository;
import peaksoft.instagrammvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void signUp(User newUser) {
        Follower follower = new Follower();
        UserInfo userInfo = new UserInfo();

        follower.setSubscriptions(new ArrayList<>());
        follower.setSubscribers(new ArrayList<>());

        newUser.setFollower(follower);
        newUser.setUserInfo(userInfo);

        userRepository.save(newUser);
    }

    @Override
    public User signIn(User user) {
        User user1=userRepository.signIn(user.getUserName(), user.getPassword());
        if (user1!=null){
           return user1;
        }
        throw new RuntimeException("Error");
    }

    @Override
    public List<User> findUserByUserName(Long userId, String keyword) {
        return userRepository.searchUsers("%" + keyword + "%");
    }

    @Override
    public void updateUser(Long id, User newUser) {
        User oldUser = findById(id);

        oldUser.setUserName(newUser.getUserName());
        oldUser.setEmail(newUser.getEmail());

        UserInfo userInfo = oldUser.getUserInfo();
        userInfo.setBiography(newUser.getUserInfo().getBiography());
        userInfo.setFullName(newUser.getUserInfo().getFullName());
        userInfo.setImage(newUser.getUserInfo().getImage());
        userInfo.setGender(newUser.getUserInfo().getGender());

        userRepository.save(oldUser);
    }

    @Override
    public User findById(Long userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new MyException(String.format("User with Id %d not found!", userId)));
        }catch (MyException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUserById( Long userId)  {
        User foundUser = findById(userId);
        userRepository.deleteById(foundUser.getId());

    }

    @Override
    public void deleteUserByPassword(String password, Long userId)  {
        User foundUser = findById(userId);
        if (foundUser.getPassword().equals(password)){
            userRepository.deleteById(userId);
        }else {
           throw new RuntimeException(String.format("User with ID %d not found !",userId));
        }
    }

    @Override
    public List<User> subscriptionsOfUser(Long userId) {
        User foundUser = findById(userId);
        List<Long> subscriptions = foundUser.getFollower().getSubscriptions();
        return userRepository.subscriptionsOfUser(subscriptions);
    }

    @Override
    public List<User> subscribersOfUser(Long userId) {
        User foundUser = findById(userId);
        List<Long> subscribers = foundUser.getFollower().getSubscribers();
        return userRepository.subscriptionsOfUser(subscribers);
    }

    @Override
    public User findOtherUserById(Long userId, Long subId) throws MyException {
        User currentUser = findById(userId);
        User foundUser = findById(subId);
        if (!foundUser.getUserName().equalsIgnoreCase(currentUser.getUserName())){
            return foundUser;
        }
        throw new MyException();
    }
}
