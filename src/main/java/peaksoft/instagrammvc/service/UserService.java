package peaksoft.instagrammvc.service;

import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.exception.MyException;

import java.util.List;

public interface UserService {

    void signUp(User newUser);
    User signIn(User user) throws MyException;

    List<User> findUserByUserName(Long userId, String keyword);

    void updateUser(Long id, User user);
    User findById(Long userId);
    void deleteUserById( Long userId);
    void deleteUserByPassword(String password,Long id)throws MyException;
    List<User> subscriptionsOfUser(Long userId);
    List<User> subscribersOfUser(Long userId);
    User findOtherUserById(Long userId, Long subId) throws MyException;
}
