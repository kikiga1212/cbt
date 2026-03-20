package com.example.cbt.Service;

import com.example.cbt.DTO.SubjectDTO;
import com.example.cbt.Entity.SubjectEntity;
import com.example.cbt.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 교과목 작업에 필요한 로직
 */
@Service
@RequiredArgsConstructor
@Transactional//삽입, 수정, 삭제적용, 작업전 백업해서 작업이 실패했을때 작업전으로 복원
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    //챕터 정보

    //등록
    public SubjectDTO createSubject(SubjectDTO dto) {
        //[1] 변환방법
        //1. modelMapper 이용했을때
        SubjectEntity entity = modelMapper.map(dto, SubjectEntity.class);
        //2. 수동으로 DTO를 Entity 변환할때
        SubjectEntity entity2 = SubjectEntity.builder()
                .subjectName(dto.getSubjectName())
                .description(dto.getDescription())
                .build();

        //[2] 저장
        SubjectEntity saved = subjectRepository.save(entity);
        //[3] 결과전달
        return toDTO(saved);
    }

    // 수정
    public SubjectDTO updateSubject(Long id, SubjectDTO dto) {
        //[1] 중복데이터 확인(유효성 검사)
        SubjectEntity entity = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("교과목이 존재하지 않습니다."));

        //[2] 조회한 데이터(Entity)에 변경된 데이터(DTO)를 전달
        entity.setSubjectName(dto.getSubjectName());
        entity.setDescription(dto.getDescription());

        //[3] 수정된 데이터저장 후 전달
        return toDTO(subjectRepository.save(entity));
    }

    // 삭제
    public void deleteSubject(Long id) {
        //[1] 삭제 대상이 존재여부를 확인
        if(!subjectRepository.existsById(id)){//삭제할 ID가 존재하지 않으면
            throw new IllegalArgumentException("교과목이 존재하지 않습니다.");
        }
        //[2] 삭제처리
        subjectRepository.deleteById(id);
    }

    //전체조회
    @Transactional(readOnly = true) //클래스에 @Transactional을 적용했으면 조회는 제외
    public List<SubjectDTO> getAllSubjents(){
        //[1] 전체조회에서 DTO로 변환해서 전달
        return subjectRepository.findAll().stream()//stream()d느 for 또는 forEach와 동일한 반복
                .map(this::toDTO)//map은 stream값을 하나씩 저장, 개별 each(개별저장될 변수 : list 변수)
                .collect(Collectors.toList());//collect는 map을 다시 저장(리스트 형식)
    }

    //개별조회(상세보기, 수정)
    @Transactional(readOnly =true)
    public SubjectDTO getSubject(Long id){
        //[1] 조회(유효성 검사)
        SubjectEntity entity = subjectRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("교과목이 존재하지 않습니다."));
        //[2] 변환
        SubjectDTO dto = toDTO(entity);
        //교과목이 해당하는 챕터를 수집

        //[3] 전달
        return dto;

    }

    //todo : 해야할 내용을 기재
    //fixMe : 수정할 내용을 기재


    //변환(Entity를 DTO 로 변환)
    private SubjectDTO toDTO(SubjectEntity entity) {
        //SubjectDTO subjectDTO = modelMapper.map(entity, SubjectDTO.class);
        //return subjectDTO;

        return SubjectDTO.builder()
                .id(entity.getId())
                .subjectName(entity.getSubjectName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
