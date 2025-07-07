package main.java.entity;

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
     * 借阅人 id
     */
    private Integer borrowerId;

    /**
     * 借阅日期
     */
    private LocalDate borrowDate;

    /**
     * 应规划日期
     */
    private LocalDate dueDate;

    /**
     * 实际归还日期
     */
    private LocalDate returnDate;

    /**
     * 状态
     */
    private String status;
}
