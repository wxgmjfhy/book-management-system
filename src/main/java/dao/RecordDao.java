package main.java.dao;

import main.java.entity.Record;
import main.java.enums.RecordStatus;
import main.java.util.FileUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RecordDao {
    // CSV 文件路径和表头
    private static final String RECORDS_CSV = "records.csv";
    private static final String RECORD_HEADER = "id,borrowerId,bookId,borrowDate,dueDate,returnDate,status,bookName";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public int insert(Record record) {
        // 生成自增 ID
        int newId = FileUtil.getNextId(RECORDS_CSV, line -> {
            String[] parts = line.split(",", -1); // 用 -1 保留空字段
            return parts.length > 0 && !parts[0].isEmpty() ? Integer.parseInt(parts[0]) : 0;
        });
        record.setId(newId);

        boolean success = FileUtil.appendCsv(RECORDS_CSV, record, this::toCsvLine);
        return success ? newId : -1;
    }

    public void deleteById(int recordId) {
        // 读取所有记录, 过滤掉要删除的 ID
        List<Record> records = listAll().stream()
                .filter(record -> !record.getId().equals(recordId))
                .collect(Collectors.toList());

        // 写回文件
        FileUtil.writeCsv(RECORDS_CSV, RECORD_HEADER, records, this::toCsvLine);
    }

    public Record getByUserAndBook(Integer userId, Integer bookId, RecordStatus status) {
        List<Record> records = listAll();
        return records.stream()
                .filter(record ->
                        record.getBorrowerId().equals(userId) &&
                                record.getBookId().equals(bookId) &&
                                (status == null || record.getStatus() == status)
                )
                .findFirst()
                .orElse(null);
    }

    public boolean update(Record record) {
        if (record.getId() == null) {
            return false;
        }

        // 读取所有记录, 更新匹配 ID 的记录
        List<Record> records = listAll();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId().equals(record.getId())) {
                records.set(i, record);
                break;
            }
        }

        // 写回文件
        return FileUtil.writeCsv(RECORDS_CSV, RECORD_HEADER, records, this::toCsvLine);
    }

    // 读取所有记录
    private List<Record> listAll() {
        return FileUtil.readCsv(RECORDS_CSV, line -> {
            String[] parts = line.split(",", -1); // -1 保留空字段
            if (parts.length != 8) {
                System.err.println("记录数据格式错误: " + line + "，字段数应为 8");
                return null;
            }

            Record record = new Record();
            try {
                record.setId(parts[0].isEmpty() ? null : Integer.parseInt(parts[0]));
                record.setBorrowerId(parts[1].isEmpty() ? null : Integer.parseInt(parts[1]));
                record.setBookId(parts[2].isEmpty() ? null : Integer.parseInt(parts[2]));
                record.setBorrowDate(parts[3].isEmpty() ? null : LocalDate.parse(parts[3], DATE_FORMATTER));
                record.setDueDate(parts[4].isEmpty() ? null : LocalDate.parse(parts[4], DATE_FORMATTER));
                record.setReturnDate(parts[5].isEmpty() ? null : LocalDate.parse(parts[5], DATE_FORMATTER));
                record.setStatus(parts[6].isEmpty() ? null : RecordStatus.valueOf(parts[6]));
                record.setBookName(parts[7].isEmpty() ? null : parts[7]);
            } catch (Exception e) {
                System.err.println("解析记录数据失败: " + line + "，错误: " + e.getMessage());
                return null;
            }
            return record;
        });
    }

    // 将 Record 对象转换为 CSV 行字符串
    private String toCsvLine(Record record) {
        return String.join(",",
                record.getId() == null ? "" : record.getId().toString(),
                record.getBorrowerId() == null ? "" : record.getBorrowerId().toString(),
                record.getBookId() == null ? "" : record.getBookId().toString(),
                record.getBorrowDate() == null ? "" : record.getBorrowDate().format(DATE_FORMATTER),
                record.getDueDate() == null ? "" : record.getDueDate().format(DATE_FORMATTER),
                record.getReturnDate() == null ? "" : record.getReturnDate().format(DATE_FORMATTER),
                record.getStatus() == null ? "" : record.getStatus().name(),
                record.getBookName() == null ? "" : record.getBookName()
        );
    }

    /**
     * 根据用户 ID 和记录状态查询记录
     */
    public List<Record> listByUserIdAndStatus(Integer userId, RecordStatus status) {
        if (userId == null || status == null) {
            return null;
        }
        return listAll().stream()
                .filter(record ->
                        userId.equals(record.getBorrowerId()) && status.equals(record.getStatus())
                )
                .collect(Collectors.toList());
    }

}