package com.example.cbt.Controller;

import com.example.cbt.DTO.SubjectDTO;
import com.example.cbt.Repository.SubjectRepository;
import com.example.cbt.Service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")//그룹화해서 보안사용시 제어가 편하다.
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;
    /*
    html로 이동할때는 return "template에 있는 파일을 지정"
    Service는 이동할 때는 return "redirect:/맵핑명"-> 현재 Controller내에
    Service에서 templates이동시 데이터값이 없거나 부족해서 오류가 발생할 확률이 높다.
     */

    /** 교과목 목록(전체조회, HTML로 이동)*/
    @GetMapping
    public String listSubjects(Model model){
        //[1] HTML에 전달할 필요한 데이터를 조회(2가지 방법)
        //1. 조회후 추가적인 작업을 하고 model 저장시
        List<SubjectDTO> dto = subjectService.getAllSubjents();
        model.addAttribute("subjects", dto);

        //2. 위의 내용을 바로 다음행에서 한행으로 단순처리
        model.addAttribute("subjects", subjectService.getAllSubjents());

        //[2] 페이지로 이동
        return "subject/list";
    }
    /** 교과목 등록 폼(HTML로 이동)*/
    @GetMapping("/new")
    public String createForm(Model model){
        //[1] 빈 DTO를 전달(유효성 검사, html object와 fields로 편하게 작업)
        // 삽입과 수정의 작업이 비슷하게 동작
        model.addAttribute("subject", new SubjectDTO());

        //[2] 페이지로 이동(페이지 이동시 절대 앞에 / 가 있으면 안된다.
        // 폴더의 파일명
        return "subject/create";
    }
    /** 교과목 등록 처리(Service)*/
    @PostMapping
    public String createSubject(@ModelAttribute SubjectDTO dto){
        //[3] 예외를 이용해서 오류 메시지를 추가->try
        try {

            //[1] 전달받은dto를 service에 보내 데이터베이스에 저장
            SubjectDTO saved = subjectService.createSubject(dto);

            //[2] 이동할 페이지를 처리하는 맵핑으로 이동
            //redirect는 반드시 앞에 / 표기 , http://주소를 구성
            return "redirect:/subjects/" + saved.getId();//상세페이지로 이동
        }catch (Exception e){//모든 오류종류를 처리
            //[4] 등록을 실패했을때 오류메시지를 가지고, 등록페이지로 이동
            return "rediredt:/subjects/new?error="+e.getMessage();
        }
    }

    /** 교과목 상세보기(HTML로 이동)*/
    @GetMapping("/{id}")
    public String viewSubjext(@PathVariable Long id, Model model){
        //[1] id를 이용해서 조회대상 조회
        SubjectDTO subject = subjectService.getSubject(id);

        //[2] 조회한 내용과 추가전 내용을 전달
        //fixMe : 기존 챕터 목록과 신규등록 챕터 정보
        model.addAttribute("subject", subject);

        //[3] 상세보기 페이지로 이동
        return "subject/view";
    }

    /** 교과목 수정 폼(HTML로 이동)*/
    @GetMapping("/edit.{id}")
    public String editForm(@PathVariable Long id, Model model){
        //[1] 대상을 조회해서 결과를 저장
        model.addAttribute("subject", subjectService.getSubject(id));

        //[2] 수정페이지로 이동
        return "subject/edit";
    }

    /** 교과목 수정 처리(Service)*/
    @PostMapping("/edit/{id}")
    public String updateSubject(@PathVariable Long id,
                                @ModelAttribute SubjectDTO dto){
        //[3] 오류발생시 오류메시지를 가지고 수정페이지로 이동
        try {
            //[1] 수정할 대상 id와 수정값을 가지고 수정처리
            subjectService.updateSubject(id, dto);
            //[2] 상세페이지로 수정한 id를 가지고 이동
            return "redirect:/subjects/" + id;//상세페이지로 이동
        }catch (Exception e){
            return "redirect:/subjects/edit."+id+"?error="+e.getMessage();
        }
    }

    /** 교과목 삭제(Service) */
    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id){

        //[1] 삭제할 id를 가지고 서비스에서 삭제 처리
        subjectService.deleteSubject(id);

        //[2] 목록페이지로 이동
        return "redirect:/subjects";
    }

    //Todo : //챕터를 작업할 내용
    //Entity -> DTO -> Repository -> Service 이후에 완성

}//end

