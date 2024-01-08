package com.example1.board.controller;

import com.example1.board.dto.CommentDTO;
import com.example1.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);
        //저장된 comment_table의 id 반환
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            // 작성 성공하면 댓글목록을 가져와서 리턴
//            댓글 목록 ==> 해당 게시글의 댓글 전체 ==> 가져올때 해당 게시글을 기준으로 가져와야함
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId()); //아이디 기준으로 찾기
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK); // ok 사인 도 같이 보내줌
        }
        else {
            return new ResponseEntity<>("해당 게시물이 존재하지 않습니다", HttpStatus.NOT_FOUND); // ok 사인 도 같이 보내줌

        }
    }
}
