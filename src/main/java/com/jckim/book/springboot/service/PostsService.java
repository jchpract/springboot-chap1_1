package com.jckim.book.springboot.service;

import com.jckim.book.springboot.domain.posts.Posts;
import com.jckim.book.springboot.domain.posts.PostsRepository;
import com.jckim.book.springboot.web.dto.PostsListResponseDTO;
import com.jckim.book.springboot.web.dto.PostsResponseDTO;
import com.jckim.book.springboot.web.dto.PostsSaveRequestDTO;
import com.jckim.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDTO requestDTO){
        return postsRepository.save(requestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO){
        Posts posts =  postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id :"+id));
        posts.update(requestDTO.getTitle(), requestDTO.getContent());

        return id;
    }

    public PostsResponseDTO findById(Long id){
        Posts entity =  postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id :"+id));

        return new PostsResponseDTO(entity);

    }

    // readOnly true 옵션을 줘서 조회기능인경우 속도를 개선시킨다.
    @Transactional(readOnly = true)
    public List<PostsListResponseDTO> findAllDesc(){


        /*
        postRespository의 findAllDesc의 반환값은 List<Posts>.
        stream (posts)의 형태를 map()을 이용해 PostsListResponseDTO로 변형후, List 컬렉션으로 다시 변환.
         */

        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDTO::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        postsRepository.delete(posts);
    }


}
