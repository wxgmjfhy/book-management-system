package main.java.dto;

/**
 * 图书查询参数 数据传输对象
 */
public class BookQueryParams {
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
     * ISBN 号
     */
    private String isbn;

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

    public BookQueryParams() {
    }

    public BookQueryParams(Integer id, String name, String authors, String isbn, String category, String language, String status) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.isbn = isbn;
        this.category = category;
        this.language = language;
        this.status = status;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
