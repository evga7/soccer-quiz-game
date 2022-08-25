package pyo.quizgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/sign-up").setViewName("signup");
        registry.addViewController("/quiz/write").setViewName("addQuiz");
        registry.addViewController("/quiz").setViewName("quiz");
        registry.addViewController("/user-messages").setViewName("userMessages");
        registry.addViewController("/user-boards").setViewName("userBoards");
        registry.addViewController("/posts").setViewName("posts");
        registry.addViewController("/post-page").setViewName("addPost");
        registry.addViewController("/error-page").setViewName("errorPage");
        registry.addViewController("/user-page").setViewName("userPage");
        registry.addViewController("/user-post-info").setViewName("userPostInfo");
        registry.addViewController("/user-comment-info").setViewName("userCommentInfo");
        registry.addViewController("/user-block-info").setViewName("userBlockInfo");
        registry.addViewController("/post").setViewName("editPost");
        registry.addViewController("/user-reports").setViewName("userReports");
    }

    //delete 사용하기위해 추가
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
