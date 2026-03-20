package com.example.cbt.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chapter")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//번호

    @Column(nullable = false)
    private Integer chapterNo;//챕터 번호

    @Column(length = 200, nullable = false)
    private String title;//제목

    @Column(columnDefinition = "TEXT")
    private String description;//설명

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createAt;

    //교과목 존재해야 챕터가 존재 가능하다.
    //교과목(부모)<->챕터(자식)
    //챕터는 반드시 교과목이 있어야만 사용할 수 있다.
    //하나의 교과목(1)은 여러개의 챕터(N)로 구성된다.
    @ManyToOne(fetch = FetchType.LAZY)//여러 챕터가 하나의 교과목과 연관관계
    @JoinColumn(name = "subject_id", nullable = false)//챕터(subject_id)<->교과목(id, 기본키)
    private SubjectEntity subject;//해당 챕터의 부모정보를 저장할 변수를 Entity로 저장
}
