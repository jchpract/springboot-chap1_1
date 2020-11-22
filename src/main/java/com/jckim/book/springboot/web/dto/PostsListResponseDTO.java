package com.jckim.book.springboot.web.dto;

// 리스트 출력에 대한 DTO
// 조회될 내용이 일단 무엇인가에 대해 생각해보자.
// 여기서는 목록(리스트) 출력에 대한 내용이므로 순번(id), 제목, 게시자, 수정된날짜이다.
// 내용은 목록에 출력할필요없으므로 생성필요없음
import com.jckim.book.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;


    // Entity를 가져와서 DTO에 해당되는 내용을 대입하는 생성자를 만듬.
    public PostsListResponseDTO(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();

    }

}
