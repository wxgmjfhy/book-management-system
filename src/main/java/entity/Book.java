package main.java.entity;

import main.java.enums.BookCategory;
import main.java.enums.BookLanguage;
import main.java.enums.BookStatus;

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
    private Double price;

    /**
     * 分类
     */
    private BookCategory category;

    /**
     * 语言
     */
    private BookLanguage language;

    /**
     * 状态
     */
    private BookStatus status;

    /**
     * 简介
     */
    private String description;

    /**
     * 借阅记录 id
     */
    private Integer recordId;

    public Book() {
    }

    public Book(Integer id, String name, String authors, String publisher, LocalDate publishDate, String isbn, Double price, BookCategory category, BookLanguage language, BookStatus status, String description, Integer recordId) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.isbn = isbn;
        this.price = price;
        this.category = category;
        this.language = language;
        this.status = status;
        this.description = description;
        this.recordId = recordId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public BookLanguage getLanguage() {
        return language;
    }

    public void setLanguage(BookLanguage language) {
        this.language = language;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", 书名: " + name + ", 作者: " + authors + ", 出版社: " + publisher + ", 出版日期: " + publishDate +
                ", ISBN: " + isbn + ", 价格: " + price + ", 分类: " + category + ", 语言: " + language +
                ", 状态: " + status + ", 简介: " + description + ", 借阅记录 ID: " + recordId;
    }
}
