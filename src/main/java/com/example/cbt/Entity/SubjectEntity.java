package com.example.cbt.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 교과목 테이블(엔티티)
 */
@Entity
@Table(name = "subject")
@Getter
@Setter
//@ToString 연관관계 작업시 무한루프 현상, 예외적용
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //일련변호

    @Column(length = 100, nullable = false)
    private String subjectName; //교과목명

    @Column(columnDefinition = "Text")//String은 255자, "TEXT"는 65535자 저장
    private String description;//설명

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;//생성날짜

    //부모를 참조하는 자식테이블의 정보 및 연관관계의 제약조건

}
