package com.example1.board.repository;


import com.example1.board.entity.BoardEntity;
import com.example1.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
//    BoardEntity 로 찾기
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
//    쿼리생성하기 jpa commenttable에서 boardentity로 찾아서 id로 정렬한 commententity 리스트 반환
}
