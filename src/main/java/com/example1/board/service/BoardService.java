package com.example1.board.service;

import com.example1.board.dto.BoardDTO;
import com.example1.board.entity.BoardEntity;
import com.example1.board.entity.BoardFileEntity;
import com.example1.board.repository.BoardFileRepository;
import com.example1.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//DTO -> Entity

//Entity -> DTO
// controller -> service -> repository
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직을 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); // 받아온 겂을 entitiy 객체에 저장
            boardRepository.save(boardEntity); //jpa 상속 메서드
        }
        else {
                //파일 첨부 있을때
            /*
                1. DTO에서 파일을 꺼냄
                2. 파일 이름 가져옴
                3. 서버 저장용 이름을 만듦
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);   //파일 엔티티를 저장하는 메서드 새로 만들어서 entity에 저장
            Long savedId = boardRepository.save(boardEntity).getId();           //이 때 아이디를 사용함
            BoardEntity board = boardRepository.findById(savedId).get();    //save id 로 아이디를 다시 조회
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                // 보드 파일 속상들을 받아옴
                String originalFilename = boardFile.getOriginalFilename();  //보드 파일에 있는 이름을 가져옴
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; //밀리초 단위 값임
                String savePath = "C:/springboot_img/" + storedFileName; // 저장 위치
                boardFile.transferTo(new File(savePath)); // 해당 경로에 파일 저장 //5번 까지임
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName); // boardFileEntity에 다시 저장
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(); // 가져 와기
        List<BoardDTO> boardDTOList = new ArrayList<>();
        //entity 를 dto 객체로 옯기기
        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }
    
    @Transactional //부모가 자식을 참조하기 때문에 transactioanl 해야함
    public BoardDTO findByid(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findByid(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page= pageable.getPageNumber() - 1;
        int pageLimit = 3; //한 페이지에 보여줄 글 갯수
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // 아이디 순으로 내림 차순 정렬해서 페이지 불러오기

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
