import csv
from Class import Book, BorrowRecord
import pandas as pd
from datetime import datetime




def read_books(csv_file):
    """读取图书信息 CSV 文件并返回对象列表"""
    books = []
    with open(csv_file, 'r', encoding='utf-8-sig') as file:
        reader = csv.DictReader(file)
        for row in reader:
            # 处理空值
            for key in row:
                if row[key] is None:
                    row[key] = ''
                row[key] = row[key].strip()
                if row[key] == '':
                    row[key] = None
            
            # 创建图书对象
            book = Book(
                row['id'],
                row['name'],
                row['author'],
                row['publisher'],
                row['publish_date'],
                row['isbn'],
                row['price'],
                row['category'],
                row['language'],
                row['status'],
                row['recordid'],
                row['description']
            )
            books.append(book)
    return books


def read_borrow_records(csv_file):
    """读取借阅记录 CSV 文件并返回对象列表"""
    records = []
    with open(csv_file, 'r', encoding='utf-8-sig') as file:
        reader = csv.DictReader(file)
        for row in reader:
            # 处理空值
            for key in row:
                if row[key].strip() == '':
                    row[key] = None
            # 创建记录对象
            record = BorrowRecord(
                row['id'],
                row['borrower_id'],
                row['book_id'],
                row['name'],
                row['borrowDate'],
                row['due_date'],
                row['return_date'],
                row['status']
            )
            records.append(record)
    return records