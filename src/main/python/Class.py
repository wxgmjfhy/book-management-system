from datetime import datetime
from parse_date import parse_date


class Book:
    def __init__(self, id, name, author, publisher, publish_date, isbn, price, category, language, status, recordid, description):
        self.id = int(id)
        self.name = name
        self.author = author
        self.publisher = publisher
        self.publish_date = parse_date(publish_date)
        self.isbn = isbn
        self.price = float(price)
        self.category = category
        self.language = language
        self.status = status
        self.recordid = int(recordid) if recordid else None
        self.description = description


class BorrowRecord:
    def __init__(self, id, borrower_id, book_id, name, borrowDate, due_date, return_date, status):
        self.id = int(id)
        self.borrower_id = int(borrower_id)
        self.book_id = int(book_id)
        self.name = name
        self.borrowDate = parse_date(borrowDate)
        self.due_date = parse_date(due_date)
        self.return_date = parse_date(return_date) if return_date else None
        self.status = status
