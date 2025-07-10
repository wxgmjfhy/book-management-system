package main.java.dao;

import main.java.dto.BookQueryParams;
import main.java.entity.Book;
import main.java.enums.BookCategory;
import main.java.enums.BookLanguage;
import main.java.enums.BookStatus;
import main.java.util.FileUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BookDao {
    // CSV 文件路径和表头
    private static final String BOOKS_CSV = "books.csv";
    private static final String BOOK_HEADER = "id,name,authors,publisher,publishDate,isbn,price,category,language,status,description,recordId";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Book getById(Integer bookId) {
        if (bookId == null) {
            return null;
        }
        List<Book> books = listAll();
        return books.stream()
                .filter(book -> bookId.equals(book.getId()))
                .findFirst()
                .orElse(null);
    }

    public List<Book> listAll() {
        return FileUtil.readCsv(BOOKS_CSV, line -> {
            String[] parts = line.split(",", -1);
            if (parts.length != 12) {
                System.err.println("图书数据格式错误: " + line);
                return null;
            }

            Book book = new Book();
            try {
                book.setId(parts[0].isEmpty() ? null : Integer.parseInt(parts[0]));
                book.setName(parts[1].isEmpty() ? null : parts[1]);
                book.setAuthors(parts[2].isEmpty() ? null : parts[2]);
                book.setPublisher(parts[3].isEmpty() ? null : parts[3]);
                book.setPublishDate(parts[4].isEmpty() ? null : LocalDate.parse(parts[4], DATE_FORMATTER));
                book.setIsbn(parts[5].isEmpty() ? null : parts[5]);
                book.setPrice(parts[6].isEmpty() ? null : Double.parseDouble(parts[6]));
                book.setCategory(parts[7].isEmpty() ? null : BookCategory.valueOf(parts[7]));
                book.setLanguage(parts[8].isEmpty() ? null : BookLanguage.valueOf(parts[8]));
                book.setStatus(parts[9].isEmpty() ? null : BookStatus.valueOf(parts[9]));
                book.setDescription(parts[10].isEmpty() ? null : parts[10]);
                book.setRecordId(parts[11].isEmpty() ? null : Integer.parseInt(parts[11]));
            } catch (Exception e) {
                System.err.println("解析图书数据失败: " + line + "，错误: " + e.getMessage());
                return null;
            }
            return book;
        });
    }

    public List<Book> selectByParams(BookQueryParams params) {
        List<Book> allBooks = listAll();
        // 按查询参数过滤
        return allBooks.stream()
                .filter(book -> filterByParams(book, params))
                .collect(Collectors.toList());
    }

    // 按查询参数过滤图书
    private boolean filterByParams(Book book, BookQueryParams params) {
        // ID 过滤
        if (params.getId() != null && !params.getId().equals(book.getId())) {
            return false;
        }

        // 书名过滤 (模糊匹配)
        if (params.getName() != null && !params.getName().isEmpty() &&
                !book.getName().toLowerCase().contains(params.getName().toLowerCase())) {
            return false;
        }

        // 作者过滤 (模糊匹配)
        if (params.getAuthors() != null && !params.getAuthors().isEmpty() &&
                !book.getAuthors().toLowerCase().contains(params.getAuthors().toLowerCase())) {
            return false;
        }

        // ISBN 过滤
        if (params.getIsbn() != null && !params.getIsbn().isEmpty() &&
                !params.getIsbn().equals(book.getIsbn())) {
            return false;
        }

        // 分类过滤
        if (params.getCategory() != null && !params.getCategory().isEmpty() &&
                !params.getCategory().equalsIgnoreCase(book.getCategory().name())) {
            return false;
        }

        // 语言过滤
        if (params.getLanguage() != null && !params.getLanguage().isEmpty() &&
                !params.getLanguage().equalsIgnoreCase(book.getLanguage().name())) {
            return false;
        }

        // 状态过滤
        if (params.getStatus() != null && !params.getStatus().isEmpty() &&
                !params.getStatus().equalsIgnoreCase(book.getStatus().name())) {
            return false;
        }

        return true;
    }

    public int insert(Book book) {
        // 生成自增 ID
        int newId = FileUtil.getNextId(BOOKS_CSV, line -> {
            String[] parts = line.split(",");
            return parts.length > 0 && !parts[0].isEmpty() ? Integer.parseInt(parts[0]) : 0;
        });
        book.setId(newId);

        // 转换 Book 为 CSV 行
        boolean success = FileUtil.appendCsv(BOOKS_CSV, book, this::toCsvLine);
        return success ? newId : -1;
    }

    public boolean update(Book book) {
        if (book.getId() == null) {
            return false;
        }
        // 读取所有图书, 更新匹配 ID 的记录
        List<Book> books = listAll();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(book.getId())) {
                // 合并更新 (保留原字段, 覆盖非空的新字段)
                Book oldBook = books.get(i);
                Book updatedBook = mergeBook(oldBook, book);
                books.set(i, updatedBook);
                break;
            }
        }
        // 写回文件
        return FileUtil.writeCsv(BOOKS_CSV, BOOK_HEADER, books, this::toCsvLine);
    }

    // 合并新旧图书信息 (非空字段覆盖)
    private Book mergeBook(Book oldBook, Book newBook) {
        Book merged = new Book();
        merged.setId(oldBook.getId()); // ID 不变
        merged.setName(newBook.getName() != null ? newBook.getName() : oldBook.getName());
        merged.setAuthors(newBook.getAuthors() != null ? newBook.getAuthors() : oldBook.getAuthors());
        merged.setPublisher(newBook.getPublisher() != null ? newBook.getPublisher() : oldBook.getPublisher());
        merged.setPublishDate(newBook.getPublishDate() != null ? newBook.getPublishDate() : oldBook.getPublishDate());
        merged.setIsbn(newBook.getIsbn() != null ? newBook.getIsbn() : oldBook.getIsbn());
        merged.setPrice(newBook.getPrice() != null ? newBook.getPrice() : oldBook.getPrice());
        merged.setCategory(newBook.getCategory() != null ? newBook.getCategory() : oldBook.getCategory());
        merged.setLanguage(newBook.getLanguage() != null ? newBook.getLanguage() : oldBook.getLanguage());
        merged.setStatus(newBook.getStatus() != null ? newBook.getStatus() : oldBook.getStatus());
        merged.setDescription(newBook.getDescription() != null ? newBook.getDescription() : oldBook.getDescription());
        merged.setRecordId(newBook.getRecordId() != null ? newBook.getRecordId() : oldBook.getRecordId());
        return merged;
    }

    public boolean deleteById(Integer bookId) {
        if (bookId == null) {
            return false;
        }
        List<Book> books = listAll().stream()
                .filter(book -> !bookId.equals(book.getId()))
                .collect(Collectors.toList());
        return FileUtil.writeCsv(BOOKS_CSV, BOOK_HEADER, books, this::toCsvLine);
    }

    public int countByIsbn(String isbn) {
        if (isbn == null) {
            return 0;
        }
        List<Book> books = listAll();
        return (int) books.stream()
                .filter(book -> isbn.equals(book.getIsbn()))
                .count();
    }

    private String toCsvLine(Book book) {
        return String.join(",",
                book.getId() == null ? "" : book.getId().toString(),
                book.getName() == null ? "" : book.getName(),
                book.getAuthors() == null ? "" : book.getAuthors(),
                book.getPublisher() == null ? "" : book.getPublisher(),
                book.getPublishDate() == null ? "" : book.getPublishDate().format(DATE_FORMATTER),
                book.getIsbn() == null ? "" : book.getIsbn(),
                book.getPrice() == null ? "" : book.getPrice().toString(),
                book.getCategory() == null ? "" : book.getCategory().name(),
                book.getLanguage() == null ? "" : book.getLanguage().name(),
                book.getStatus() == null ? "" : book.getStatus().name(),
                book.getDescription() == null ? "" : book.getDescription(),
                book.getRecordId() == null ? "" : book.getRecordId().toString()
        );
    }
}