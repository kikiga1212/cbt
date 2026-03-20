package com.example.cbt.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private Long id;
    private String subjectName;
    private String description;
    private LocalDateTime createdAt;

    //교과목 작업시 해당 자식에 챕터 정보
}
