package com.jckim.book.springboot.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jckim.book.springboot.domain.posts.Posts;
import com.jckim.book.springboot.domain.posts.PostsRepository;
import com.jckim.book.springboot.web.dto.PostsSaveRequestDTO;
import com.jckim.book.springboot.web.dto.PostsUpdateRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @WebMvcTest는 JPA 적용이 안된다. @SpringBootTest를 이용해서 JPA 테스트 시도.
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    /* MockMvc 사용하기 위해 새로 생성한 부분 */
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    @Before // 테스트가 시작되기전에 인스턴스 생성하는 어노테이션
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /* MockMvc 종료 */



    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER") // @WithMockUser MockMvc 적용되어야 사용가능함. @SpringBootTest로만 가지고서는 실행불가능.
    public void Post_등록테스트() throws Exception{
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDTO requestDTO = PostsSaveRequestDTO.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:"+ port +"/api/v1/posts";


        //when
        /* 원래 코드 */
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDTO, Long.class);

        /* 바뀐 MockMvc 코드 */
        mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(requestDTO)))
                    .andExpect(status().isOk());

        //then
        /* 원래 코드 */
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

        /* then 부분 새로 추가된것 없음 */

    }


    // 수정기능 테스트 메소드 만들자
    @Test
    @WithMockUser(roles = "USER")
    public void Post_수정테스트() throws Exception{
        //given

        //처음은 당연히 수정하려면 저장되어있는 값이 있어야하므로 등록을 먼저한다.
        Posts savedPosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        // 등록되어 있는 id를 찾아오고, 새로 수정될 제목, 내용을 만들어줌
        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        // 수정될 내용이 담긴 DTO 빌더 생성
        PostsUpdateRequestDTO requestDTO = PostsUpdateRequestDTO.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        // ResponseEntity에 들어갈 url, HttpEntity 생성
        String url = "http://localhost:"+port+"/api/v1/posts/"+updateId;
        HttpEntity<PostsUpdateRequestDTO> requestEntity = new HttpEntity<PostsUpdateRequestDTO>(requestDTO);

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk());


        //then

        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //System.out.println("출력되는내용 : "+responseEntity.getBody());
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);


    }

}
