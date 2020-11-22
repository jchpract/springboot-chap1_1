package com.jckim.book.springboot.web;

import com.jckim.book.springboot.service.PostsService;
import com.jckim.book.springboot.web.dto.PostsResponseDTO;
import com.jckim.book.springboot.web.dto.PostsSaveRequestDTO;
import com.jckim.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts") // 등록(INSERT) 절차. 해당 URL로 POST Request하면 save 메소드 실행.
    public Long save(@RequestBody PostsSaveRequestDTO requestDTO){
        return postsService.save(requestDTO);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDTO requestDTO){
        return postsService.update(id, requestDTO);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id){
        return postsService.findById(id);
    }
}


/*
*  Spring에서 Bean을 주입받는 방식 3가지
*   @Autowired - 권장하지않음
*   setter
*   생성자 - 이방법을 통해 생성하는것이 좋음
* */