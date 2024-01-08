package com.example1.board.controller;

import com.example1.board.dto.BoardDTO;
import com.example1.board.service.BoardService;
import com.example1.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController_ {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<BoardDTO>> findAll() {
        List<BoardDTO> boardDTOList = boardService.findAll();
        return new ResponseEntity<>(boardDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> findById(@PathVariable Long id) {
        BoardDTO boardDTO = boardService.findByid(id);
        return new ResponseEntity<>(boardDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return new ResponseEntity<>("Board saved successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        boardDTO.setId(id);
        BoardDTO updatedBoard = boardService.update(boardDTO);
        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return new ResponseEntity<>("Board deleted successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<BoardDTO>> paging(@PageableDefault(page = 1) Pageable pageable) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }
}
