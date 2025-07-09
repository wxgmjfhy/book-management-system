import random
import csv
from datetime import datetime, timedelta
from faker import Faker
import math
import copy

# 初始化 Faker 对象，用于生成真实数据
fake = Faker('zh_CN')

def logemee(x , z):
    if (z-x >= 5):
        x /= 5
    y = math.log2(1 + x)
    return (int)(y)

# 定义枚举类
class BookCategory:
    COMPUTER = "计算机"
    LITERATURE = "文学"
    HISTORY = "历史"
    SCIENCE = "科学"
    ART = "艺术"
    OTHER = "其他"

class BookStatus:
    AVAILABLE = "可借阅"
    BORROWED = "已借出"
    LOST = "丢失"

class RecordStatus:
    BORROWED = "已借出"
    RETURNED = "已归还"
    OVERDUE = "逾期未还"

class book_language:
    CHINESE = "中文"
    ENGLISH = "英文"
    

# 生成随机书籍信息
def generate_book(book_id, name=None):
    if not name:
        name = fake.catch_phrase()  # 生成类似书名的短语
    
    return {
        'id': book_id,
        'name': name,
        'author': fake.name(),
        'publisher': fake.company(),
        'publish_date': fake.date_between(start_date='-5y', end_date='-3m'),
        'isbn': fake.isbn13(),
        'price': round(random.uniform(20, 200), 2),
        'category': random.choices([
            BookCategory.COMPUTER, 
            BookCategory.LITERATURE, 
            BookCategory.HISTORY,
            BookCategory.SCIENCE, 
            BookCategory.ART, 
            BookCategory.OTHER
        ],weights=[0.2, 0.3, 0.15, 0.1, 0.05, 0.2],k=1),
        'language': random.choice([book_language.CHINESE, book_language.ENGLISH]),
        'status': BookStatus.AVAILABLE,
        'recordid': None,  # 初始时没有借阅记录
        'description': None
    }

# 生成每天的借阅记录
def generate_daily_borrow_records(date, book_list, borrower_ids, min_records_per_day=20):
    records = []
    date_str = date.strftime('%Y-%m-%d')
    
    # 确保每天至少有min_records_per_day条记录
    num_records = max(min_records_per_day, random.randint(min_records_per_day, min_records_per_day * 2))
    
    for _ in range(num_records):
        # 随机选择借阅者和书籍
        borrower_id = random.choice(borrower_ids)
        
        # 查找可用的书籍
        available_books = [book for book in book_list if book['status'] == BookStatus.AVAILABLE]
        
        # 如果没有可用书籍，跳过本次借阅
        if not available_books:
            break
            
        book = random.choice(available_books)
        
        # 计算应还日期（7-30天后）
        due_date = date + timedelta(days=30)
        
        is_returned = random.random() < 0.992

        if datetime.now().date() - date <= timedelta(days=40):
            is_returned = random.random() < 0.94
        if datetime.now().date() - date <= timedelta(days=30):
            is_returned = random.random() < 0.66
        if datetime.now().date() - date <= timedelta(days=20):
            is_returned = random.random() < 0.2
        if datetime.now().date() - date <= timedelta(days=10):
            is_returned = random.random() < 0.05
        
        if is_returned:
            # 归还日期：应还日期前后波动
            return_date = due_date + timedelta(days=random.randint(-5, 10))
            # 如果归还日期晚于应还日期，标记为逾期
            # status = RecordStatus.OVERDUE if return_date > due_date else RecordStatus.RETURNED
            status = RecordStatus.RETURNED
            # 书籍状态在归还后变为可借阅
            book['status'] = BookStatus.AVAILABLE
        else:
            return_date = None
            status = RecordStatus.BORROWED
            # 如果应还日期已过，标记为逾期
            if due_date < datetime.now().date():
                status = RecordStatus.OVERDUE
                
            # 书籍状态在借出后变为已借出
            book['status'] = BookStatus.BORROWED
        
        records.append({
            'borrower_id': borrower_id,
            'book_id': book['id'],
            'name': book['name'],
            'borrowDate': date,
            'due_date': due_date,
            'return_date': return_date,
            'status': status
        })
    
    return records

def main():
    # 配置参数
    num_unique_books = 2600  # 不同书名的数量
    copies_per_book = 200     # 每本书的副本数
    num_borrowers = 900     # 借阅者数量
    days_back = 400         # 生成过去多少天的记录
    
    # 生成书籍数据
    books = []
    book_id = 1
    
    # 生成不同书名的书籍
    unique_books = [generate_book(book_id + i) for i in range(num_unique_books)]
    
    # 为每本书创建多个副本（相同书名，不同ID）
    for unique_book in unique_books:
        z = logemee(random.randint(1, copies_per_book) , copies_per_book)
        # print(z)
        BK = generate_book(book_id, unique_book['name'])
        for _ in range(random.randint(1, z+1)):
            books.append(copy.deepcopy(BK))  # 深拷贝以避免引用同一对象
            book_id += 1
            BK['id'] = book_id
    
     # 生成借阅者ID列表
    borrower_ids = list(range(1, num_borrowers + 1))
    
    # 生成日期范围
    end_date = datetime.now().date()
    start_date = end_date - timedelta(days=days_back)
    date_range = [start_date + timedelta(days=i) for i in range(days_back + 1)]
    
    all_records = []
    record_id = 1

    for date in date_range:
        daily_records = generate_daily_borrow_records(date, books, borrower_ids , 95)
        
        # 更新记录ID
        for record in daily_records:
            record['id'] = record_id
            record_id += 1
        
        all_records.extend(daily_records)
    
    print("开始写入书籍数据至 CSV 文件...")
    # 写入书籍数据到CSV
    with open('./src/main/resources/books.csv', 'w', newline='', encoding='utf-8-sig') as csvfile:
        fieldnames = ['id', 'name', 'author', 'publisher', 'publish_date', 'isbn', 'price', 'category', 'language' , 'status' , 'recordid', 'description']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        
        for book in books:
            writer.writerow({
                'id': book['id'],
                'name': book['name'],
                'author': book['author'],
                'publisher': book['publisher'],
                'publish_date': book['publish_date'],
                'isbn': book['isbn'],
                'price': book['price'],
                'category': book['category'],
                'language': book['language'],
                'status': book['status'],
                'recordid': book['recordid'],
                'description': ''
            })
    

    print("开始写入借阅数据至 CSV 文件...")

    # 写入借阅记录数据到CSV
    with open('./src/main/resources/records.csv', 'w', newline='', encoding='utf-8-sig') as csvfile:
        fieldnames = ['id', 'borrower_id', 'book_id', 'name', 'borrowDate', 'due_date', 'return_date', 'status']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        
        for record in all_records:
            writer.writerow({
                'id': record['id'],
                'borrower_id': record['borrower_id'],
                'book_id': record['book_id'],
                'name': record['name'],
                'borrowDate': record['borrowDate'],
                'due_date': record['due_date'],
                'return_date': record['return_date'] or '',
                'status': record['status']
            })
    
    print(f"已生成 {len(books)} 本图书和 {len(all_records)} 条借阅记录")
    print("数据已写入 books.csv 和 borrow_records.csv 文件")

if __name__ == "__main__":
    main()