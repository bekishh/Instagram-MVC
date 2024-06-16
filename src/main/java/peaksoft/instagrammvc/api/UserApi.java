package peaksoft.instagrammvc.api;

import peaksoft.instagrammvc.entity.Post;
import peaksoft.instagrammvc.entity.User;
import peaksoft.instagrammvc.exception.MyException;
import peaksoft.instagrammvc.service.PostService;
import peaksoft.instagrammvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/page")
    public String signPage(Model model) {
        model.addAttribute("login", new User());
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute("login") User user, Model model) {
        try {
            User myUser = userService.signIn(user);
            return "redirect:/api/main/" + myUser.getId();
        } catch (MyException e) {
            model.addAttribute("errorMessage", "Incorrect userName or password, Please white correct");
            return "error-page";
        }
    }


    @GetMapping("/main/{userId}")
    private String checkingSome(Model model, @PathVariable Long userId) {
        List<User> users = userService.subscriptionsOfUser(userId);
        List<Post> allPosts = postService.findAllPosts();
        Collections.reverse(allPosts);
        model.addAttribute("allPosts", allPosts);
        model.addAttribute("userId", userId);
        model.addAttribute("subscriptions", users);
        return "home-page";
    }


    @GetMapping("/new")
    public String createAccount(Model model) {
        model.addAttribute("newUser", new User());
        return "signUp";
    }

    @PostMapping("/save")
    public String signUp(@ModelAttribute("newUser") User user) {
        userService.signUp(user);
        return "redirect:/api/main/" + user.getId();

    }

    @GetMapping("/delUserWIthId/{userId}")
    public String deleteUserById(@PathVariable Long userId,
                                 Model model) {
        model.addAttribute("curUser", userId);
        return "deleteUser-page";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUserById(@PathVariable Long userId,
                                 @RequestParam String password,
                                 Model model) {
        try {
            userService.deleteUserByPassword(password, userId);
            return "redirect:/api/page";
        } catch (MyException e) {
            model.addAttribute("errorMessage", "Incorrect password, Please white correct");
            return "error-page";
        }
    }
}

