### 📘 교과목 테이블 (SubjectEntity)
| 필드명          | 타입                   | 설명                                              |
| ------------ | -------------------- | ----------------------------------------------- |
| `id`         | Long                 | 교과목 고유 ID (PK, 자동 생성)                           |
| `name`       | String               | 교과목명 (최대 100자, 필수)                              |
| `description`| String               | 교과목 설명 (TEXT 타입, 선택 입력)                         |
| `createdAt`  | LocalDateTime        | 등록일시 (자동 생성, 수정 불가)                             |
| `chapters`   | List\<ChapterEntity\>  | 하위 챕터 목록 (1:N 관계, Cascade.ALL, orphanRemoval 적용) |

---

### 📘 챕터 테이블 (ChapterEntity)
| 필드명          | 타입                    | 설명                                               |
| ------------ | --------------------- | ------------------------------------------------ |
| `id`         | Long                  | 챕터 고유 ID (PK, 자동 생성)                            |
| `chapterNo`  | Integer               | 챕터 번호 (정렬 기준, 필수)                               |
| `title`      | String                | 챕터명 (최대 200자, 필수)                               |
| `description`| String                | 챕터 설명 (TEXT 타입, 선택 입력)                          |
| `createdAt`  | LocalDateTime         | 등록일시 (자동 생성, 수정 불가)                             |
| `subject`    | SubjectEntity         | 소속 교과목 (N:1 관계, 필수, `subject_id` FK)            |
| `questions`  | List\<QuestionEntity\>  | 하위 문제 목록 (1:N 관계, Cascade.ALL, orphanRemoval 적용) |

---

### 📘 문제 테이블 (QuestionEntity)
| 필드명            | 타입            | 설명                                                                         |
| -------------- | ------------- | -------------------------------------------------------------------------- |
| `id`           | Long          | 문제 고유 ID (PK, 자동 생성)                                                      |
| `content`      | String        | 문제 내용 (TEXT 타입, 필수)                                                       |
| `questionType` | String        | 문제 유형 (MULTIPLE_CHOICE: 4지선다, SHORT_ANSWER: 필답형, ESSAY: 주관식, 최대 20자, 필수) |
| `option1`      | String        | 4지선다 보기 ① (TEXT 타입, MULTIPLE_CHOICE일 때 사용)                                |
| `option2`      | String        | 4지선다 보기 ② (TEXT 타입, MULTIPLE_CHOICE일 때 사용)                                |
| `option3`      | String        | 4지선다 보기 ③ (TEXT 타입, MULTIPLE_CHOICE일 때 사용)                                |
| `option4`      | String        | 4지선다 보기 ④ (TEXT 타입, MULTIPLE_CHOICE일 때 사용)                                |
| `answer`       | String        | 정답 (TEXT 타입, MULTIPLE_CHOICE: 1~4 중 하나, SHORT_ANSWER/ESSAY: 모범답안 텍스트, 필수) |
| `explanation`  | String        | 해설 (TEXT 타입, 선택 입력)                                                       |
| `point`        | Integer       | 배점 (기본값 1, 필수)                                                            |
| `createdAt`    | LocalDateTime | 등록일시 (자동 생성, 수정 불가)                                                       |
| `updatedAt`    | LocalDateTime | 수정일시 (자동 갱신)                                                              |
| `chapter`      | ChapterEntity | 소속 챕터 (N:1 관계, 필수, `chapter_id` FK)                                      |

---

### 📘 평가 테이블 (ExamEntity)
| 필드명            | 타입                       | 설명                                                          |
| -------------- | ------------------------ | ----------------------------------------------------------- |
| `id`           | Long                     | 평가 고유 ID (PK, 자동 생성)                                       |
| `examineeName` | String                   | 응시자 이름 (최대 100자, 필수)                                       |
| `examDate`     | LocalDateTime            | 평가 시작 일시 (자동 생성, 수정 불가)                                    |
| `status`       | String                   | 평가 상태 (IN_PROGRESS: 진행중, SUBMITTED: 제출완료, GRADED: 채점완료, 기본값: IN_PROGRESS, 최대 20자) |
| `subject`      | SubjectEntity            | 평가 교과목 (N:1 관계, 필수, `subject_id` FK)                       |
| `answers`      | List\<ExamAnswerEntity\>   | 제출 답안 목록 (1:N 관계, Cascade.ALL, orphanRemoval 적용)           |
| `totalScore`   | Integer                  | 최종 획득 점수 (채점 완료 후 저장, 선택)                                  |
| `maxScore`     | Integer                  | 만점 기준 점수 (채점 완료 후 저장, 선택)                                  |

---

### 📘 답안 테이블 (ExamAnswerEntity)
| 필드명               | 타입            | 설명                                                   |
| ----------------- | ------------- | ---------------------------------------------------- |
| `id`              | Long          | 답안 고유 ID (PK, 자동 생성)                                |
| `exam`            | ExamEntity    | 소속 평가 (N:1 관계, 필수, `exam_id` FK)                    |
| `question`        | QuestionEntity | 해당 문제 (N:1 관계, 필수, `question_id` FK)                |
| `submittedAnswer` | String        | 응시자가 제출한 답안 (TEXT 타입, 선택 입력)                        |
| `isCorrect`       | Boolean       | 정답 여부 (4지선다: 자동채점, SHORT_ANSWER/ESSAY: 수동채점 후 설정)   |
| `earnedPoint`     | Integer       | 획득 점수 (채점 후 저장, 선택)                                 |
| `gradingComment`  | String        | 채점 의견 (TEXT 타입, SHORT_ANSWER/ESSAY 수동채점 시 작성, 선택)   |

---

### 📘 시작 API 정의서 (HomeController)
| 기능         | Method | 경로  | 설명                       | 요청 파라미터 / 요청 바디 | 반환 / 처리                                                                   |
| ---------- | ------ | --- | ------------------------ | --------------- | ------------------------------------------------------------------------- |
| **메인 페이지** | GET    | `/` | 대시보드 메인 페이지로 이동          | 없음              | `index.html` 뷰 렌더링 / `subjectCount`, `questionCount`, `examCount`, `gradedCount` 모델 전달 |

---

### 📘 과목 API 정의서 (SubjectController)
| 기능               | Method | 경로                                              | 설명                          | 요청 파라미터 / 요청 바디                              | 반환 / 처리                                                          |
| ---------------- | ------ | ----------------------------------------------- | --------------------------- | -------------------------------------------- | ---------------------------------------------------------------- |
| **교과목 목록 조회**    | GET    | `/subjects`                                     | 전체 교과목 조회                   | 없음                                           | `subject/list.html` 뷰 렌더링, `subjects` 모델 전달                     |
| **교과목 등록 폼**     | GET    | `/subjects/new`                                 | 교과목 등록 입력 폼 페이지             | 없음                                           | `subject/create.html` 뷰 렌더링, 빈 `SubjectDTO` 모델 전달               |
| **교과목 등록 처리**    | POST   | `/subjects`                                     | 교과목 등록 후 상세 페이지로 이동         | Form Data: `SubjectDTO`                      | 성공 시 `/subjects/{id}`로 리다이렉트, 실패 시 `/subjects/new?error=메시지`로 리다이렉트 |
| **교과목 상세 조회**    | GET    | `/subjects/{id}`                                | 특정 교과목 및 하위 챕터 목록 조회        | PathVariable: `id`                           | `subject/view.html` 뷰 렌더링, `subject`, `chapters`, `newChapter` 모델 전달 |
| **교과목 수정 폼**     | GET    | `/subjects/edit/{id}`                           | 교과목 수정 입력 폼 페이지             | PathVariable: `id`                           | `subject/edit.html` 뷰 렌더링, `subject` 모델 전달                      |
| **교과목 수정 처리**    | POST   | `/subjects/edit/{id}`                           | 교과목 수정 후 상세 페이지로 이동         | PathVariable: `id`, Form Data: `SubjectDTO`  | 성공 시 `/subjects/{id}`로 리다이렉트, 실패 시 `/subjects/edit/{id}?error=메시지`로 리다이렉트 |
| **교과목 삭제**       | GET    | `/subjects/delete/{id}`                         | 교과목 삭제 후 목록으로 이동 (하위 챕터·문제 연쇄 삭제) | PathVariable: `id`                    | `/subjects`로 리다이렉트                                               |
| **챕터 등록 처리**     | POST   | `/subjects/{subjectId}/chapters`                | 챕터 등록 후 교과목 상세 페이지로 이동      | PathVariable: `subjectId`, Form Data: `ChapterDTO` | 성공 시 `/subjects/{subjectId}`로 리다이렉트, 실패 시 `?error=메시지`로 리다이렉트  |
| **챕터 수정 폼**      | GET    | `/subjects/{subjectId}/chapters/edit/{chapterId}` | 챕터 수정 입력 폼 페이지           | PathVariable: `subjectId`, `chapterId`       | `subject/chapter-edit.html` 뷰 렌더링, `subject`, `chapter` 모델 전달   |
| **챕터 수정 처리**     | POST   | `/subjects/{subjectId}/chapters/edit/{chapterId}` | 챕터 수정 후 교과목 상세 페이지로 이동  | PathVariable: `subjectId`, `chapterId`, Form Data: `ChapterDTO` | 성공 시 `/subjects/{subjectId}`로 리다이렉트, 실패 시 `?error=메시지`로 리다이렉트 |
| **챕터 삭제**        | GET    | `/subjects/{subjectId}/chapters/delete/{chapterId}` | 챕터 삭제 후 교과목 상세 페이지로 이동 | PathVariable: `subjectId`, `chapterId`       | `/subjects/{subjectId}`로 리다이렉트                                    |

---

### 📘 문제 API 정의서 (QuestionController)
| 기능             | Method | 경로                           | 설명                       | 요청 파라미터 / 요청 바디                               | 반환 / 처리                                                              |
| -------------- | ------ | ---------------------------- | ------------------------ | --------------------------------------------- | -------------------------------------------------------------------- |
| **교과목 선택 화면**  | GET    | `/questions`                 | 문제 관리용 교과목 선택 목록 화면      | 없음                                            | `question/subjects.html` 뷰 렌더링, `subjects` 모델 전달                    |
| **챕터 선택 화면**   | GET    | `/questions/subject/{subjectId}` | 선택한 교과목의 챕터 목록 화면    | PathVariable: `subjectId`                     | `question/chapters.html` 뷰 렌더링, `subject`, `chapters` 모델 전달          |
| **문제 목록 조회**   | GET    | `/questions/chapter/{chapterId}` | 선택한 챕터의 문제 목록 화면     | PathVariable: `chapterId`                     | `question/list.html` 뷰 렌더링, `chapter`, `questions` 모델 전달             |
| **문제 등록 폼**    | GET    | `/questions/new/{chapterId}` | 문제 등록 입력 폼 페이지           | PathVariable: `chapterId`                     | `question/create.html` 뷰 렌더링, 빈 `QuestionDTO`, `chapter` 모델 전달       |
| **문제 등록 처리**   | POST   | `/questions/new/{chapterId}` | 문제 등록 후 챕터별 문제 목록으로 이동   | PathVariable: `chapterId`, Form Data: `QuestionDTO` | 성공 시 `/questions/chapter/{chapterId}`로 리다이렉트, 실패 시 `?error=메시지`로 리다이렉트 |
| **문제 수정 폼**    | GET    | `/questions/edit/{id}`       | 문제 수정 입력 폼 페이지           | PathVariable: `id`                            | `question/edit.html` 뷰 렌더링, `question`, `chapter` 모델 전달              |
| **문제 수정 처리**   | POST   | `/questions/edit/{id}`       | 문제 수정 후 챕터별 문제 목록으로 이동   | PathVariable: `id`, Form Data: `QuestionDTO` | 성공 시 `/questions/chapter/{chapterId}`로 리다이렉트, 실패 시 `?error=메시지`로 리다이렉트 |
| **문제 삭제**      | GET    | `/questions/delete/{id}`     | 문제 삭제 후 챕터별 문제 목록으로 이동   | PathVariable: `id`                            | `/questions/chapter/{chapterId}`로 리다이렉트                               |

---

### 📘 평가진행 API 정의서 (ExamController)
| 기능            | Method | 경로                          | 설명                      | 요청 파라미터 / 요청 바디                               | 반환 / 처리                                                    |
| ------------- | ------ | --------------------------- | ----------------------- | --------------------------------------------- | ---------------------------------------------------------- |
| **교과목 선택 화면** | GET    | `/exam`                     | 평가 진행할 교과목 선택 목록 화면     | 없음                                            | `exam/select.html` 뷰 렌더링, `subjects` 모델 전달                 |
| **응시자 정보 입력** | GET    | `/exam/start/{subjectId}`   | 응시자 이름 입력 폼 페이지         | PathVariable: `subjectId`                     | `exam/start.html` 뷰 렌더링, `subject` 모델 전달                   |
| **평가 시작 처리**  | POST   | `/exam/start/{subjectId}`   | 평가 생성 후 문제 풀이 화면으로 이동   | PathVariable: `subjectId`, RequestParam: `examineeName` | `/exam/take/{examId}`로 리다이렉트                   |
| **평가 진행 화면**  | GET    | `/exam/take/{examId}`       | 문제 목록 및 답안 입력 화면        | PathVariable: `examId`                        | `exam/take.html` 뷰 렌더링, `exam`, `questions` 모델 전달          |
| **답안 제출 처리**  | POST   | `/exam/submit/{examId}`     | 전체 답안 제출 후 결과 화면으로 이동   | PathVariable: `examId`, Form Data: `answer_{questionId}` 형태 동적 파라미터 | `/result/{examId}`로 리다이렉트 |

---

### 📘 평가결과 API 정의서 (ResultController)
| 기능           | Method | 경로                       | 설명                               | 요청 파라미터 / 요청 바디                                                  | 반환 / 처리                                                               |
| ------------ | ------ | ------------------------ | -------------------------------- | ------------------------------------------------------------------ | ----------------------------------------------------------------------- |
| **결과 목록 조회** | GET    | `/result`                | 전체 또는 교과목별·상태별 평가 결과 목록 조회       | RequestParam(optional): `subjectId`, `status`                      | `result/list.html` 뷰 렌더링, `exams`, `subjects`, 필터 조건 모델 전달             |
| **결과 상세 조회** | GET    | `/result/{examId}`       | 특정 평가의 문항별 답안 및 채점 결과 상세 조회      | PathVariable: `examId`                                             | `result/view.html` 뷰 렌더링, `exam`, `needsGrading` 모델 전달                |
| **수동 채점 폼**  | GET    | `/result/grade/{examId}` | 필답형·주관식 문항 수동 채점 입력 폼 페이지        | PathVariable: `examId`                                             | `result/grade.html` 뷰 렌더링, `exam` 모델 전달                               |
| **수동 채점 처리** | POST   | `/result/grade/{examId}` | 채점 점수 저장 및 총점 계산 후 결과 상세 페이지로 이동 | PathVariable: `examId`, Form Data: `score_{questionId}`, `comment_{questionId}` 형태 동적 파라미터 | `/result/{examId}`로 리다이렉트 |

---

### 📘 과목 로직 정의서 (SubjectService)
| 함수 이름            | 매개변수                      | 반환형               | 설명                      | 특징 / 비고                                     |
| ---------------- | ------------------------- | ----------------- | ----------------------- | ------------------------------------------- |
| `createSubject`  | `SubjectDTO dto`          | `SubjectDTO`      | 신규 교과목 등록 후 DTO 반환      | Entity 직접 빌더 생성; 저장 후 DTO 변환                |
| `getAllSubjects`  | 없음                        | `List<SubjectDTO>` | 전체 교과목 목록 조회 후 DTO 리스트 반환 | `chapterCount` 포함 변환                        |
| `getSubject`     | `Long id`                 | `SubjectDTO`      | 단건 교과목 조회 후 챕터 목록 포함 DTO 반환 | 존재하지 않으면 `IllegalStateException` 발생; `chapters` 리스트 포함 |
| `updateSubject`  | `Long id, SubjectDTO dto` | `SubjectDTO`      | 교과목 정보 수정 후 DTO 반환      | 존재 여부 확인 후 `name`, `description` 수정          |
| `deleteSubject`  | `Long id`                 | `void`            | 교과목 삭제 (하위 챕터·문제 연쇄 삭제) | 존재하지 않으면 `IllegalStateException` 발생; Cascade.ALL로 연쇄 삭제 |
| `toDTO` (private)| `SubjectEntity entity`    | `SubjectDTO`      | Entity → DTO 변환        | `chapterCount` 포함                           |

---

### 📘 챕터 로직 정의서 (ChapterService)
| 함수 이름             | 매개변수                           | 반환형              | 설명                      | 특징 / 비고                                             |
| ----------------- | ------------------------------ | ---------------- | ----------------------- | --------------------------------------------------- |
| `createChapter`   | `Long subjectId, ChapterDTO dto` | `ChapterDTO`   | 신규 챕터 등록 후 DTO 반환       | 교과목 존재 여부 확인; 빌더로 Entity 생성                         |
| `getChaptersBySubject` | `Long subjectId`          | `List<ChapterDTO>` | 교과목별 챕터 목록 조회 (챕터번호 오름차순) | `questionCount` 포함 변환                          |
| `getChapter`      | `Long id`                      | `ChapterDTO`     | 단건 챕터 조회 후 DTO 반환       | 존재하지 않으면 `IllegalStateException` 발생                  |
| `updateChapter`   | `Long id, ChapterDTO dto`      | `ChapterDTO`     | 챕터 정보 수정 후 DTO 반환       | `chapterNo`, `title`, `description` 수정             |
| `deleteChapter`   | `Long id`                      | `void`           | 챕터 삭제 (하위 문제 연쇄 삭제)     | 존재하지 않으면 `IllegalStateException` 발생; Cascade.ALL로 연쇄 삭제 |
| `toDTO` (private) | `ChapterEntity entity`         | `ChapterDTO`     | Entity → DTO 변환        | `subjectId`, `subjectName`, `questionCount` 포함      |

---

### 📘 문제 로직 정의서 (QuestionService)
| 함수 이름                    | 매개변수                           | 반환형                 | 설명                         | 특징 / 비고                                                              |
| ------------------------ | ------------------------------ | ------------------- | -------------------------- | -------------------------------------------------------------------- |
| `createQuestion`         | `Long chapterId, QuestionDTO dto` | `QuestionDTO`    | 신규 문제 등록 후 DTO 반환          | 챕터 존재 여부 확인; 유형에 따라 보기(option1~4) 저장; 배점 기본값 1                      |
| `getQuestionsByChapter`  | `Long chapterId`               | `List<QuestionDTO>` | 챕터별 문제 목록 조회 후 DTO 리스트 반환  | Entity → DTO 변환                                                      |
| `getQuestionsBySubject`  | `Long subjectId`               | `List<QuestionDTO>` | 교과목 전체 문제 조회 후 DTO 리스트 반환  | JPQL 사용; 챕터 번호 → 문제 ID 순 정렬                                         |
| `getQuestion`            | `Long id`                      | `QuestionDTO`       | 단건 문제 조회 후 DTO 반환          | 존재하지 않으면 `IllegalStateException` 발생                                  |
| `updateQuestion`         | `Long id, QuestionDTO dto`     | `QuestionDTO`       | 문제 정보 수정 후 DTO 반환          | 모든 필드 갱신 가능; 배점 null인 경우 기존값 유지                                     |
| `deleteQuestion`         | `Long id`                      | `void`              | 문제 삭제                      | 존재하지 않으면 `IllegalStateException` 발생                                  |
| `toDTO` (private)        | `QuestionEntity entity`        | `QuestionDTO`       | Entity → DTO 변환            | `chapterId`, `chapterTitle`, `subjectId`, `subjectName` 포함           |

---

### 📘 평가 로직 정의서 (ExamService)
| 함수 이름           | 매개변수                                                         | 반환형            | 설명                              | 특징 / 비고                                                                                        |
| --------------- | ------------------------------------------------------------ | -------------- | ------------------------------- | ---------------------------------------------------------------------------------------------- |
| `startExam`     | `Long subjectId, String examineeName`                        | `ExamDTO`      | 신규 평가 생성 후 DTO 반환               | 교과목 존재 여부 확인; 상태 `IN_PROGRESS`로 초기화                                                           |
| `submitAnswers` | `Long examId, Map<Long, String> answerMap`                   | `void`         | 전체 답안 저장 및 4지선다 자동 채점          | 4지선다(`MULTIPLE_CHOICE`): 정답 비교 후 `isCorrect`, `earnedPoint` 자동 설정; 필답형/주관식: `isCorrect=null`, `earnedPoint=null`로 저장; 평가 상태를 `SUBMITTED`로 변경 |
| `gradeManual`   | `Long examId, Map<Long, Integer> scoreMap, Map<Long, String> commentMap` | `void` | 수동 채점 점수 및 의견 저장 후 총점 계산 | 해당 답안의 `earnedPoint`, `gradingComment`, `isCorrect` 갱신; 전체 합산 후 `totalScore`, `maxScore` 저장; 상태를 `GRADED`로 변경 |
| `getExamResult` | `Long examId`                                                | `ExamDTO`      | 평가 결과 상세 조회 (답안 포함)            | 존재하지 않으면 `IllegalStateException` 발생; 문항별 `ExamAnswerDTO` 리스트 포함                                |
| `getAllExams`    | 없음                                                           | `List<ExamDTO>` | 전체 평가 목록 조회 (최신순)             | `examDate` 내림차순 정렬                                                                            |
| `getExamsBySubject` | `Long subjectId`                                         | `List<ExamDTO>` | 교과목별 평가 목록 조회 (최신순)           | `examDate` 내림차순 정렬                                                                            |
| `getExamsByStatus` | `String status`                                           | `List<ExamDTO>` | 상태별 평가 목록 조회                   | `IN_PROGRESS`, `SUBMITTED`, `GRADED` 중 하나로 필터링                                                |
| `toDTO` (private) | `ExamEntity entity`                                        | `ExamDTO`      | Entity → DTO 변환                | `subjectId`, `subjectName`, `totalScore`, `maxScore` 포함; SUBMITTED/GRADED 상태일 때 maxScore 계산   |

---

### 📘 교과목 쿼리 정의서 (SubjectRepository)
| 메서드 이름              | JPQL / SQL 동작 설명                | 입력 파라미터         | 반환 타입                 | 특징 / 비고           |
| ------------------- | -------------------------------- | --------------- | --------------------- | ----------------- |
| `findByNameContaining` | 교과목명(`name`)에 특정 문자열이 포함된 교과목 조회 | `String name`   | `List<SubjectEntity>` | 부분 검색, 대소문자 민감도는 DB 설정에 따라 다름 |
| `existsByName`      | 특정 교과목명(`name`) 존재 여부 확인         | `String name`   | `boolean`             | 교과목명 중복 체크용       |

---

### 📘 챕터 쿼리 정의서 (ChapterRepository)
| 메서드 이름                              | JPQL / SQL 동작 설명                          | 입력 파라미터                                    | 반환 타입                  | 특징 / 비고                         |
| ----------------------------------- | ----------------------------------------- | ------------------------------------------ | ---------------------- | ------------------------------- |
| `findBySubjectOrderByChapterNoAsc`  | 특정 교과목 Entity의 챕터 목록을 챕터번호 오름차순으로 조회     | `SubjectEntity subject`                    | `List<ChapterEntity>`  | 교과목 Entity 직접 참조, 정렬 포함          |
| `findBySubjectIdOrderByChapterNoAsc`| 특정 교과목 ID의 챕터 목록을 챕터번호 오름차순으로 조회         | `Long subjectId`                           | `List<ChapterEntity>`  | ID 기반 조회, 정렬 포함                  |
| `existsBySubjectAndChapterNo`       | 특정 교과목 내 특정 챕터번호 존재 여부 확인                 | `SubjectEntity subject, Integer chapterNo` | `boolean`              | 동일 교과목 내 챕터번호 중복 체크용            |

---

### 📘 문제 쿼리 정의서 (QuestionRepository)
| 메서드 이름                          | JPQL / SQL 동작 설명                              | 입력 파라미터                               | 반환 타입                   | 특징 / 비고                        |
| -------------------------------- | --------------------------------------------- | ------------------------------------- | ----------------------- | ------------------------------ |
| `findByChapter`                  | 특정 챕터 Entity에 속한 문제 목록 조회                     | `ChapterEntity chapter`               | `List<QuestionEntity>` | 챕터 Entity 직접 참조                |
| `findByChapterId`                | 특정 챕터 ID에 속한 문제 목록 조회                         | `Long chapterId`                      | `List<QuestionEntity>` | ID 기반 조회                       |
| `findByChapterIdAndQuestionType` | 특정 챕터 ID와 문제 유형으로 필터링된 문제 목록 조회               | `Long chapterId, String questionType` | `List<QuestionEntity>` | AND 조건 결합                      |
| `findBySubjectId`                | 특정 교과목 ID에 속한 전체 문제 조회 (챕터번호 → 문제 ID 순 정렬)   | `Long subjectId`                      | `List<QuestionEntity>` | JPQL 사용; 평가진행 시 전체 문제 불러오기에 사용 |
| `countByChapterId`               | 특정 챕터 ID에 속한 문제 수 조회                          | `Long chapterId`                      | `long`                 | JPQL COUNT 사용                  |

---

### 📘 평가 쿼리 정의서 (ExamRepository)
| 메서드 이름                                      | JPQL / SQL 동작 설명                   | 입력 파라미터          | 반환 타입              | 특징 / 비고                  |
| ------------------------------------------- | ---------------------------------- | ---------------- | ------------------ | ------------------------ |
| `findBySubjectIdOrderByExamDateDesc`        | 특정 교과목의 평가 목록 조회 (최신순)             | `Long subjectId` | `List<ExamEntity>` | `examDate` 내림차순 정렬       |
| `findByExamineeNameContainingOrderByExamDateDesc` | 응시자명 부분 검색 후 최신순으로 조회        | `String examineeName` | `List<ExamEntity>` | 부분 검색, 최신순 정렬         |
| `findAllByOrderByExamDateDesc`              | 전체 평가 목록을 최신순으로 조회                 | 없음               | `List<ExamEntity>` | `examDate` 내림차순 정렬       |
| `findByStatus`                              | 특정 상태(`status`)의 평가 목록 조회          | `String status`  | `List<ExamEntity>` | `IN_PROGRESS`, `SUBMITTED`, `GRADED` 중 하나로 필터링 |

---

### 📘 답안 쿼리 정의서 (ExamAnswerRepository)
| 메서드 이름                          | JPQL / SQL 동작 설명              | 입력 파라미터                           | 반환 타입                       | 특징 / 비고                    |
| -------------------------------- | ----------------------------- | --------------------------------- | --------------------------- | -------------------------- |
| `findByExamId`                   | 특정 평가 ID에 속한 전체 답안 목록 조회      | `Long examId`                     | `List<ExamAnswerEntity>`    | 평가 결과 조회 및 채점 처리에 사용       |
| `findByExamIdAndQuestionId`      | 특정 평가의 특정 문제 답안 단건 조회         | `Long examId, Long questionId`    | `Optional<ExamAnswerEntity>` | 답안 중복 저장 방지; 기존 답안 업데이트 시 사용 |
