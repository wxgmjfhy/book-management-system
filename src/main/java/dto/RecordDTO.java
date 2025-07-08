package main.java.dto;

import java.time.LocalDate;

/**
 * 借阅记录 数据传输对象
 */
public class RecordDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 图书 id
     */
    private Integer bookId;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 借阅日期
     */
    private LocalDate borrowDate;

    /**
     * 应归还日期
     */
    private LocalDate dueDate;

    public RecordDTO() {
    }

    public RecordDTO(Integer id, Integer bookId, String bookName, LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", 图书 ID: " + bookId + ", 书名: " + bookName + ", 借阅日期: " + borrowDate + ", 应归还日期: " + dueDate;
    }
}
