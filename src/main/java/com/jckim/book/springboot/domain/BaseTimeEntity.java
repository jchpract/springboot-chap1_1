package com.jckim.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 해당 클래스를 상속할경우 안에 있는 필드들을 추가적으로 칼럼으로 인식하게함.
@EntityListeners(AuditingEntityListener.class) // 클래스에 Auditing 기능 포함한다.
public abstract class BaseTimeEntity {
    // 추상클래스로 선언되었음.

    @CreatedDate // Entity 생성되어 저장될시 시간이 자동저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회된 Entity 값이 변경될때 시간 자동저장
    private LocalDateTime modifiedDate;

}
