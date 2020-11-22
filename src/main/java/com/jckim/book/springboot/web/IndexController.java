package com.jckim.book.springboot.web;


import com.jckim.book.springboot.config.auth.LoginUser;
import com.jckim.book.springboot.config.auth.dto.SessionUser;
import com.jckim.book.springboot.service.PostsService;
import com.jckim.book.springboot.web.dto.PostsResponseDTO;
import com.jckim.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    //private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){

        model.addAttribute("posts", postsService.findAllDesc());
      //  SessionUser user = (SessionUser)httpSession.getAttribute("user");

        if(user != null){

            System.out.println(user.getEmail());
            System.out.println(user.getName());
            model.addAttribute("userName1", user.getName());
            /*
            *  model attribute 추가할때 userName이라는 이름으로 추가하면
            *  아무래도 윈도우를 서버로 사용해서 그런지 user.getName()이 아니라 윈도우 환경변수에 있는 %USERNAME% 값을 읽어들이는것같다.
            *  userName1로 변경했더니 정상출력된다.
            * */

        }
        return "index";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDTO dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }


}
