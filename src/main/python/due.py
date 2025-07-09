from Class import Book, BorrowRecord
from datetime import datetime
import copy
import tkinter as tk
import tkinter.ttk as ttk

def almost_due(books, borrow_records):
    """检查借阅记录中是否有即将到期的图书"""
    due_records = []
    for record in borrow_records:
        due_date = record.due_date
        return_date = record.return_date
        
        # 检查是否在即将到期的范围内
        if due_date and return_date is None:
            days_left = (due_date - datetime.now().date()).days
            if 0 <= days_left <= 1:  # 1天内到期
                due_records.append(copy.deepcopy(record))
    return due_records

class AlmostDueWindow:
    """显示即将逾期记录的窗口"""
    def __init__(self, root, books, records):
        self.root = root
        self.books = books
        self.records = records
        
        self.window = tk.Toplevel(root)
        self.window.title("即将逾期的借阅记录")
        self.window.geometry("900x600")
        
        # 创建标题标签
        self.title_label = tk.Label(self.window, text="即将逾期的借阅记录", font=("SimHei", 16, "bold"))
        self.title_label.pack(pady=20)
        
        # 创建表格框架
        self.table_frame = tk.Frame(self.window)
        self.table_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
        
        # 创建统计信息框架
        self.stats_frame = tk.Frame(self.window)
        self.stats_frame.pack(fill=tk.X, padx=20, pady=10)
        
        # 创建表格
        self.create_table()
        
        # 填充数据
        self.populate_data()
    
    def create_table(self):
        """创建表格"""
        columns = ("记录ID", "借阅者ID", "书名", "借阅日期", "应还日期", "剩余天数", "状态")
        self.tree = ttk.Treeview(self.table_frame, columns=columns, show="headings")
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=100, anchor=tk.CENTER)
        
        self.tree.column("书名", width=200)
        
        scrollbar = ttk.Scrollbar(self.table_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.pack(fill=tk.BOTH, expand=True)
    
    def populate_data(self):
        """填充数据到表格"""
        due_records = almost_due(self.books, self.records)
        
        # 统计信息
        total_records = len(due_records)
        overdue_today = sum(1 for r in due_records if (r.due_date - datetime.now().date()).days == 0)
        overdue_tomorrow = sum(1 for r in due_records if (r.due_date - datetime.now().date()).days == 1)
        
        # 显示统计信息
        stats_label = tk.Label(
            self.stats_frame, 
            text=f"总计: {total_records} 条即将逾期记录 | 今天到期: {overdue_today} | 明天到期: {overdue_tomorrow}",
            font=("SimHei", 10)
        )
        stats_label.pack(anchor=tk.W)
        
        # 清空表格
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        # 填充表格
        for record in due_records:
            days_left = (record.due_date - datetime.now().date()).days
            status = "今天到期" if days_left == 0 else f"{days_left}天后到期"
            
            # 根据剩余天数设置不同颜色
            tag = ""
            if days_left == 0:
                tag = "today"
            elif days_left == 1:
                tag = "tomorrow"
            
            self.tree.insert("", tk.END, values=(
                record.id,
                record.borrower_id,
                record.name,
                record.borrowDate,
                record.due_date,
                days_left,
                status
            ), tags=(tag,))
        
        # 设置标签样式
        self.tree.tag_configure("today", background="#ffcccc")  # 红色背景表示今天到期
        self.tree.tag_configure("tomorrow", background="#ffebcc")  # 橙色背景表示明天到期