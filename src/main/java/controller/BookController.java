package main.java.controller;

import main.java.dto.BookQueryParams;
import main.java.dto.RecordDTO;
import main.java.entity.Book;
import main.java.entity.User;
import main.java.enums.BookCategory;
import main.java.enums.BookLanguage;
import main.java.enums.BookStatus;
import main.java.service.BookService;
import main.java.service.RecordService;
import main.java.util.InputUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BookController {

    private final BookService bookService = new BookService();
    private final RecordService recordService = new RecordService();

    public void bookOperations(User user) {
        if (user.isStudent()) {
            bookOperationsOfStudent(user);
        } else {
            bookOperationsOfAdmin();
        }
    }

    private void bookOperationsOfStudent(User user) {
        while (true) {
            System.out.println("-- 学生图书操作菜单 --");
            System.out.println("1. 借书 (根据 ID)");
            System.out.println("2. 还书 (根据 ID)");
            System.out.println("3. 查看我的借阅");
            System.out.println("4. 查看所有图书");
            System.out.println("5. 搜索图书");
            System.out.println("6. 返回上一级菜单");

            int choice = InputUtil.readInt("请输入选择:");

            switch (choice) {
                case 1:
                    borrowBook(user);
                    break;
                case 2:
                    returnBook(user);
                    break;
                case 3:
                    listRecordsOfUser(user);
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    selectBooks();
                    break;
                case 6:
                    System.out.println("-- 返回上一级菜单 --");
                    return;
                default:
                    System.out.println("无效选择, 请重新输入!");
            }
        }
    }

    private void bookOperationsOfAdmin() {
        while (true) {
            System.out.println("-- 管理员图书操作菜单 --");
            System.out.println("1. 添加图书");
            System.out.println("2. 删除图书 (根据 ID)");
            System.out.println("3. 修改图书 (根据 ID)");
            System.out.println("4. 查看所有图书");
            System.out.println("5. 搜索图书");
            System.out.println("6. 返回上一级菜单");

            int choice = InputUtil.readInt("请输入选择:");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    selectBooks();
                    break;
                case 6:
                    System.out.println("-- 返回上一级菜单 --");
                    return;
                default:
                    System.out.println("无效选择, 请重新输入!");
            }
        }
    }

    private void borrowBook(User user) {
        Integer id = InputUtil.readInteger("请输入要借阅的图书 ID:");
        if (id == null) {
            System.out.println("取消操作");
            return;
        }
        String message = bookService.borrowBook(user, id);
        System.out.println(message);
    }

    private void returnBook(User user) {
        Integer id = InputUtil.readInteger("请输入要归还的图书 ID:");
        if (id == null) {
            System.out.println("取消操作");
            return;
        }
        String message = bookService.returnBook(user, id);
        System.out.println(message);
    }

    private void listRecordsOfUser(User user) {
        List<RecordDTO> recordDTOList = recordService.list(user);
        if (recordDTOList.isEmpty()) {
            System.out.println("无借阅记录");
            return;
        }
        recordDTOList.forEach(System.out::println);
    }

    private void listAllBooks() {
        List<Book> bookList = bookService.list();
        if (bookList.isEmpty()) {
            System.out.println("无图书记录");
            return;
        }
        bookList.forEach(System.out::println);
    }

    private void selectBooks() {
        System.out.println("-- 搜索图书 --");
        BookQueryParams params = new BookQueryParams();

        params.setId(InputUtil.readInteger("请输入图书 ID (可直接回车跳过):"));
        params.setName(InputUtil.readString("请输入书名 (可直接回车跳过):"));
        params.setAuthors(InputUtil.readString("请输入作者 (可直接回车跳过):"));
        params.setIsbn(InputUtil.readString("请输入 ISBN (可直接回车跳过):"));
        params.setCategory(InputUtil.readString("请输入分类 (可直接回车跳过):"));
        params.setLanguage(InputUtil.readString("请输入语言 (可直接回车跳过):"));
        params.setStatus(InputUtil.readString("请输入状态 (可直接回车跳过):"));

        List<Book> books = bookService.selectBooks(params);

        if (books.isEmpty()) {
            System.out.println("未找到匹配的图书!");
        } else {
            System.out.println("共查询到 " + books.size() + " 本图书:");
            books.forEach(System.out::println);
        }
    }

    private void addBook() {
        System.out.println("-- 添加图书 --");

        // 创建新的 Book 对象
        Book book = new Book();

        // 读取并设置书名
        book.setName(InputUtil.readString("请输入书名 (可直接回车跳过):"));

        // 读取并设置作者
        book.setAuthors(InputUtil.readString("请输入作者 (可直接回车跳过):"));;

        // 读取并设置出版社
        book.setPublisher(InputUtil.readString("请输入出版社 (可直接回车跳过):"));

        // 读取并设置出版日期
        book.setPublishDate(InputUtil.readDate("请输入出版日期 (格式: yyyy-MM-dd) (可直接回车跳过):"));

        // 读取并设置 ISBN
        book.setIsbn(InputUtil.readString("请输入 ISBN (可直接回车跳过):"));

        // 读取并设置价格
        book.setPrice(InputUtil.readDouble("请输入价格 (可直接回车跳过):"));

        // 读取并设置分类
        System.out.println("可选分类: " + String.join(", ",
                Arrays.stream(BookCategory.values()).map(Enum::name).toArray(String[]::new)));
        String categoryStr = InputUtil.readString("请输入分类 (可直接回车跳过):");
        if (categoryStr != null && !categoryStr.isEmpty()) {
            try {
                book.setCategory(BookCategory.valueOf(categoryStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("分类输入无效, 已忽略");
            }
        }

        // 读取并设置语言
        System.out.println("可选语言: " + String.join(", ",
                Arrays.stream(BookLanguage.values()).map(Enum::name).toArray(String[]::new)));
        String languageStr = InputUtil.readString("请输入语言 (可直接回车跳过):");
        if (languageStr != null && !languageStr.isEmpty()) {
            try {
                book.setLanguage(BookLanguage.valueOf(languageStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("语言输入无效, 已忽略");
            }
        }

        // 设置默认状态为可借阅
        book.setStatus(BookStatus.AVAILABLE);

        // 读取并设置简介
        book.setDescription(InputUtil.readString("请输入图书简介 (可直接回车跳过):"));

        // 调用服务层添加图书
        String message = bookService.addBook(book);
        System.out.println(message);
    }

    private void deleteBook() {
        Integer id = InputUtil.readInteger("请输入要删除的图书 ID (可直接回车取消):");
        if (id == null) {
            System.out.println("取消操作");
            return;
        }
        String message = bookService.deleteBook(id);
        System.out.println(message);
    }

    private void updateBook() {
        Integer id = InputUtil.readInteger("请输入要更新的图书 ID (可直接回车取消):");
        if (id == null) {
            System.out.println("取消操作");
            return;
        }

        Book book = new Book();
        book.setId(id);

        System.out.println("提示: 直接回车表示不修改该属性!");

        String name = InputUtil.readString("请输入新书名:");
        if (name != null && !name.isEmpty()) {
            book.setName(name);
        }

        String authors = InputUtil.readString("请输入新作者:");
        if (authors != null && !authors.isEmpty()) {
            book.setAuthors(authors);
        }

        String publisher = InputUtil.readString("请输入新出版社:");
        if (publisher != null && !publisher.isEmpty()) {
            book.setPublisher(publisher);
        }

        LocalDate publishDate = InputUtil.readDate("请输入新出版日期 (格式: yyyy-MM-dd):");
        if (publishDate != null) {
            book.setPublishDate(publishDate);
        }

        String isbn = InputUtil.readString("请输入新 ISBN:");
        if (isbn != null && !isbn.isEmpty()) {
            book.setIsbn(isbn);
        }

        Double price = InputUtil.readDouble("请输入新价格:");
        if (price != null) {
            book.setPrice(price);
        }

        // 分类更新
        System.out.println("可选分类: " + String.join(", ",
                Arrays.stream(BookCategory.values()).map(Enum::name).toArray(String[]::new)));
        String categoryStr = InputUtil.readString("请输入新分类:");
        if (categoryStr != null && !categoryStr.isEmpty()) {
            try {
                book.setCategory(BookCategory.valueOf(categoryStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("分类输入无效, 已忽略");
            }
        }

        // 语言更新
        System.out.println("可选语言: " + String.join(", ",
                Arrays.stream(BookLanguage.values()).map(Enum::name).toArray(String[]::new)));
        String languageStr = InputUtil.readString("请输入新语言:");
        if (languageStr != null && !languageStr.isEmpty()) {
            try {
                book.setLanguage(BookLanguage.valueOf(languageStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("语言输入无效, 已忽略");
            }
        }

        // 状态更新
        System.out.println("可选状态: " + String.join(", ",
                Arrays.stream(BookStatus.values()).map(Enum::name).toArray(String[]::new)));
        String statusStr = InputUtil.readString("请输入新状态:");
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                book.setStatus(BookStatus.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("状态输入无效, 已忽略");
            }
        }

        String description = InputUtil.readString("请输入新简介:");
        if (description != null && !description.isEmpty()) {
            book.setDescription(description);
        }

        String message = bookService.updateBook(book);
        System.out.println(message);
    }

}
