package com.roman.service.telegram.help;

import com.roman.dao.entity.Post;
import com.roman.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.roman.service.telegram.help.HelpMessage.DIRECTOR_POST_HELP_MESSAGE;
import static com.roman.service.telegram.help.HelpMessage.NO_POST_HELP_MESSAGE;
import static com.roman.service.telegram.help.HelpMessage.WORKER_POST_HELP_MESSAGE;

@Service
@RequiredArgsConstructor
public class HelpService {

    private final PostService postService;

    public String checkUserPost(User user){
        Long userId = user.getId();
        Optional<Post> mayBePost = postService.findWorkerPost(userId);
        return mayBePost.isEmpty() ? ifPostEmpty(mayBePost) : ifPostPresent(mayBePost);
    }

    private String ifPostEmpty(Optional<Post> mayBePost){
        return NO_POST_HELP_MESSAGE;
    }

    private String ifPostPresent(Optional<Post> post){
        String postTitle = post.get().getTitle();
        return postTitle.equals("director") ? DIRECTOR_POST_HELP_MESSAGE : WORKER_POST_HELP_MESSAGE;
    }


}
