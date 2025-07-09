import datetime
from datetime import datetime, timedelta
from collections import Counter
from read import read_books, read_borrow_records
import parse_date
import copy
def generate_book_ranking(records, books, top_n=10, time_range=None):
    """生成图书借阅排行榜"""
    # 按时间范围筛选记录
    if time_range:
        end_date = datetime.now().date()
        start_date = end_date - timedelta(days=time_range)
        filtered_records = [r for r in records if r.borrowDate and r.borrowDate >= start_date]
    else:
        filtered_records = records

    
    # 统计每本书的借阅次数
    book_counter = Counter(r.name for r in filtered_records)
    
    # 构建图书ID到图书对象的映射
    book_map = {book.name: book for book in books}
    
    # 生成排行榜
    ranking = []
    for book_name, count in book_counter.most_common(top_n):
        if book_name in book_map:
            book = book_map[book_name]
            ranking.append({
                'rank': len(ranking) + 1,
                'book_name': book_name,
                # 'title': book.name,
                'author': book.author,
                'borrow_count': count
            })
    
    return ranking

def generate_borrower_ranking(records, top_n=10, time_range=None):
    """生成借阅者排行榜"""
    # 按时间范围筛选记录
    if time_range:
        end_date = datetime.now().date()
        start_date = end_date - timedelta(days=time_range)
        filtered_records = [r for r in records if r.borrowDate and r.borrowDate >= start_date]
    else:
        filtered_records = records
    
    # 统计每个借阅者的借阅次数
    borrower_counter = Counter(r.borrower_id for r in filtered_records)
    
    # 生成排行榜
    ranking = []
    for borrower_id, count in borrower_counter.most_common(top_n):
        ranking.append({
            'rank': len(ranking) + 1,
            'borrower_id': borrower_id,
            'borrow_count': count
        })
    
    return ranking

def print_ranking(ranking, title):
    """打印排行榜"""
    print(f"\n{'='*20} {title} {'='*20}")
    if not ranking:
        print("暂无数据")
        return
    
    # 动态计算列宽
    max_title_len = max(len(item.get('book_name', '')) for item in ranking)
    max_author_len = max(len(item.get('author', '')) for item in ranking)
    
    # 打印表头
    if 'book_name' in ranking[0]:  # 图书排行榜
        print(f"排名\t书名{' '*(max_title_len-4)}\t作者{' '*(max_author_len-2)}\t借阅次数")
        for item in ranking:
            title_padding = ' ' * (max_title_len - len(item['book_name']))
            author_padding = ' ' * (max_author_len - len(item['author']))
            print(f"{item['rank']}\t{item['book_name']}{title_padding}\t{item['author']}{author_padding}\t{item['borrow_count']}")
    else:  # 借阅者排行榜
        print(f"排名\t借阅者ID\t借阅次数")
        for item in ranking:
            print(f"{item['rank']}\t{item['borrower_id']}\t\t{item['borrow_count']}")

def main():
    # 读取数据
    books = read_books('./src/main/resources/books.csv')
    records = read_borrow_records('./src/main/resources/records.csv')
    
    print(f"已读取 {len(books)} 本图书和 {len(records)} 条借阅记录")
    
    # 生成并打印图书借阅排行榜（全部时间）
    book_ranking_all = generate_book_ranking(records, books, top_n=10)
    print_ranking(book_ranking_all, "最受欢迎的图书（全部时间）")
    
    # 生成并打印图书借阅排行榜（最近30天）
    book_ranking_30d = generate_book_ranking(records, books, top_n=10, time_range=30)
    print_ranking(book_ranking_30d, "最受欢迎的图书（最近30天）")
    
    # 生成并打印借阅者排行榜（全部时间）
    borrower_ranking_all = generate_borrower_ranking(records, top_n=10)
    print_ranking(borrower_ranking_all, "最活跃的借阅者（全部时间）")
    
    # 生成并打印借阅者排行榜（最近90天）
    borrower_ranking_90d = generate_borrower_ranking(records, top_n=10, time_range=90)
    print_ranking(borrower_ranking_90d, "最活跃的借阅者（最近90天）")

if __name__ == "__main__":
    main()