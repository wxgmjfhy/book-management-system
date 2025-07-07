package main.java.entity;

import java.time.LocalDate;

/**
 * 图书 实体类
 */
public class Book {
    /**
     * id
     */
    private Integer id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String authors;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 出版日期
     */
    private LocalDate publishDate;

    /**
     * ISBN 号
     */
    private String isbn;

    /**
     * 价格
     */
    private double price;

    /**
     * 分类
     */
    private String category;

    /**
     * 语言
     */
    private String language;

    /**
     * 状态
     */
    private String status;

    /**
     * 简介
     */
    private String description;

    /**
     * 借阅记录 id
     */
    private Integer borrowingRecordId;
}
