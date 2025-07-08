package main.java.service;

import main.java.dto.BookQueryParams;
import main.java.entity.Book;
import main.java.entity.Record;
import main.java.entity.User;
import main.java.enums.BookStatus;
import main.java.enums.RecordStatus;
import main.java.dao.BookDao;
import main.java.dao.RecordDao;

import java.time.LocalDate;
import java.util.List;

public class BookService {

    private final BookDao bookDao = new BookDao();
    private final RecordDao recordDao = new RecordDao();

    public String borrowBook(User user, Integer bookId) {
        // 1. 校验图书是否存在
        Book book = bookDao.getById(bookId);
        if (book == null) {
            return "错误: 图书不存在";
        }

        // 2. 校验图书状态
        if (book.getStatus() != BookStatus.AVAILABLE) {
            return "错误：图书当前状态为 [" + book.getStatus() + "], 无法借阅";
        }

        // 3. 创建借阅记录, 默认借阅期限 30天
        Record record = new Record();
        record.setBookId(bookId);
        record.setBookName(book.getName());
        record.setBorrowerId(user.getId());
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(30)); // 30天后到期
        record.setStatus(RecordStatus.BORROWED);
        int recordId = recordDao.insert(record);
        if (recordId <= 0) {
            return "错误: 借阅记录创建失败";
        }

        // 4. 更新图书状态为已借出, 并关联借阅记录 ID
        book.setStatus(BookStatus.BORROWED);
        book.setRecordId(recordId);
        boolean updateSuccess = bookDao.update(book);
        if (!updateSuccess) {
            // 回滚: 删除已创建的借阅记录
            recordDao.deleteById(recordId);
            return "错误: 图书状态更新失败, 借阅失败";
        }

        return "借阅成功！ 应归还日期: " + record.getDueDate();
    }

    public String returnBook(User user, Integer bookId) {
        // 1. 检查是否存在有效的借阅记录
        Record record = recordDao.getByUserAndBook(user.getId(), bookId, RecordStatus.BORROWED);
        if (record == null) {
            return "错误: 未找到借阅记录";
        }

        // 2. 更新借阅记录状态为已归还, 记录归还日期
        record.setReturnDate(LocalDate.now());
        record.setStatus(RecordStatus.RETURNED);
        boolean updateRecordSuccess = recordDao.update(record);
        if (!updateRecordSuccess) {
            return "错误: 归还记录更新失败";
        }

        // 3. 更新图书状态为可借阅, 清除关联的记录 ID
        Book book = bookDao.getById(bookId);
        book.setStatus(BookStatus.AVAILABLE);
        book.setRecordId(null);
        boolean updateBookSuccess = bookDao.update(book);
        if (!updateBookSuccess) {
            // 回滚: 恢复记录状态
            record.setReturnDate(null);
            record.setStatus(RecordStatus.BORROWED);
            recordDao.update(record);
            return "错误: 图书状态更新失败, 还书失败";
        }

        return "还书成功！";
    }

    public List<Book> list() {
        return bookDao.listAll();
    }

    public String deleteBook(Integer bookId) {
        if (bookId == null) {
            return "错误: 图书 ID 不能为空";
        }

        // 1. 检查图书是否存在
        Book book = bookDao.getById(bookId);
        if (book == null) {
            return "错误: 图书不存在";
        }

        // 2. 检查图书是否已被借阅
        if (book.getStatus() == BookStatus.BORROWED) {
            return "错误: 图书当前处于 [已借出] 状态, 无法删除";
        }

        // 3. 执行删除
        boolean success = bookDao.deleteById(bookId);
        return success ? "删除图书成功" : "错误: 删除图书失败";
    }

    public String updateBook(Book book) {
        if (book.getId() == null) {
            return "错误: 图书 ID 不能为空";
        }

        // 1. 检查图书是否存在
        Book existingBook = bookDao.getById(book.getId());
        if (existingBook == null) {
            return "错误: 图书不存在";
        }

        // 2. 执行更新
        boolean success = bookDao.update(book);
        return success ? "更新图书成功" : "错误：更新图书失败";
    }

    public List<Book> selectBooks(BookQueryParams params) {
        return bookDao.selectByParams(params);
    }

    public String addBook(Book book) {
        // 1. 冲突校验
        if (book.getIsbn() != null && bookDao.countByIsbn(book.getIsbn()) > 0) {
            return "错误: ISBN 已存在, 无法重复添加";
        }

        // 2. 执行添加
        int bookId = bookDao.insert(book);
        return bookId > 0 ? "添加图书成功! 新图书 ID: " + bookId : "错误: 添加图书失败";
    }
}