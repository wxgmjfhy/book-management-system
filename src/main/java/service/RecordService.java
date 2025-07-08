package main.java.service;

import main.java.dao.BookDao;
import main.java.dao.RecordDao;
import main.java.dto.RecordDTO;
import main.java.entity.Book;
import main.java.entity.Record;
import main.java.entity.User;
import main.java.enums.RecordStatus;
import java.util.List;
import java.util.stream.Collectors;

public class RecordService {
    private final RecordDao recordDao = new RecordDao();

    /**
     * 查询用户的当前借阅记录
     */
    public List<RecordDTO> list(User user) {
        List<Record> currentRecords = recordDao.listByUserIdAndStatus(user.getId(), RecordStatus.BORROWED);
        return currentRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Record 实体 -> RecordDTO
     */
    private RecordDTO convertToDTO(Record record) {
        RecordDTO dto = new RecordDTO();
        dto.setId(record.getId());
        dto.setBookId(record.getBookId());
        dto.setBookName(record.getBookName());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        return dto;
    }
}