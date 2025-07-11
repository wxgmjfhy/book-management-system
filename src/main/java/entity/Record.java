package main.java.entity;

import main.java.enums.RecordStatus;

import java.time.LocalDate;

/**
 * 借阅记录 实体类
 */
public class Record {
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
     * 借阅人 id
     */
    private Integer borrowerId;

    /**
     * 借阅日期
     */
    private LocalDate borrowDate;

    /**
     * 应归还日期
     */
    private LocalDate dueDate;

    /**
     * 实际归还日期
     */
    private LocalDate returnDate;

    /**
     * 状态
     */
    private RecordStatus status;

    public Record() {
    }

    public Record(Integer id, Integer bookId, String bookName, Integer borrowerId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, RecordStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
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

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }
}
