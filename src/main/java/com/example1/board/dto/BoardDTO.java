package com.example1.board.dto;

import com.example1.board.entity.BoardEntity;
import com.example1.board.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter //get set method 자동으로 해줌
@ToString
@NoArgsConstructor      //기본 생성자
@AllArgsConstructor     //모든 필드 매개변수 생성자
//ㄹㅇ lombok 개꿀
//Data Transfer Object (vo, Bean) : 데이터 전송 객체
public class BoardDTO {
        //필드들
        private Long id;
        private String boardWriter;
        private String boardPass;
        private String boardTitle;
        private String boardContents;
        private int boardHits;
        private LocalDateTime boardCreatedTime;
        private LocalDateTime boardUpdatedime;

        private List<MultipartFile> boardFile; // 파일 속성 값 받아오기 위함 이름, 파일 속성 등 여러 정보를 가지고 있음
        private List<String> originalFileName;        //원본 파일 이름
        private List<String> storedFileName;          //서버 저장용 파일 이름
        private int fileAttached;               //파일 첨부 여부(첨부1, 미첨부 0)

        public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
                this.id = id;
                this.boardWriter = boardWriter;
                this.boardTitle = boardTitle;
                this.boardHits = boardHits;
                this.boardCreatedTime = boardCreatedTime;
        }

        public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setId(boardEntity.getId());
                boardDTO.setBoardWriter(boardEntity.getBoardWriter());
                boardDTO.setBoardPass(boardEntity.getBoardPass());
                boardDTO.setBoardTitle(boardEntity.getBoardTitle());
                boardDTO.setBoardHits(boardEntity.getBoardHits());
                boardDTO.setBoardContents(boardEntity.getBoardContents());
                boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
                boardDTO.setBoardUpdatedime(boardEntity.getUpdatedTime());

                if (boardEntity.getFileAttached() == 0) {
                        boardDTO.setFileAttached(boardEntity.getFileAttached());
                } else {
                        List<String> originalFileNameList = new ArrayList<>();        //원본 파일 이름
                        List<String> storedFileNameList = new ArrayList<>();
                        boardDTO.setFileAttached(boardEntity.getFileAttached());
                        // 파일 이름 가져오기
                        // filname들은 boardfileEntity에 들어있음 근데 가져온거는 endtitiy 에서 가져온거임
                        // 서로 다른 테이블에 있는 값을 가져오려면 join 을 써야함
                        for (BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()) {
                                originalFileNameList.add(boardFileEntity.getOriginalFileName()); //자식 테이블 참조
                                storedFileNameList.add(boardFileEntity.getStoredFileName());    //자식 테이블 참조
                        }
                        boardDTO.setOriginalFileName(originalFileNameList);
                        boardDTO.setStoredFileName(storedFileNameList);

                }
                return boardDTO;
        }
}
