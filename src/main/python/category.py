import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from collections import Counter
from datetime import datetime, timedelta
plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置默认字体

def category_borrow_stats(records, books, time_range=None):
    """统计不同种类书籍的借阅情况"""
    # 筛选时间范围内的记录
    if time_range:
        end_date = datetime.now().date()
        start_date = end_date - timedelta(days=time_range)
        filtered_records = [r for r in records if r.borrowDate and r.borrowDate >= start_date]
    else:
        filtered_records = records
    
    # 创建图书ID到类别的映射
    book_category_map = {book.id: book.category for book in books}
    
    # 统计各类别的借阅次数
    category_counter = Counter()
    for record in filtered_records:
        if record.book_id in book_category_map:
            category = book_category_map[record.book_id]
            category_counter[category] += 1
    
    return category_counter

class CategoryStatsWindow:
    """显示类别统计的窗口"""
    def __init__(self, root, books, records, time_range=None):
        self.root = root
        self.books = books
        self.records = records
        self.time_range = time_range
        
        self.window = tk.Toplevel(root)
        if time_range:
            self.window.title(f"30天内书籍类别借阅统计")
        else:
            self.window.title(f"全部时间书籍类别借阅统计")
        self.window.geometry("900x600")
        
        # 创建标题标签
        if time_range:
            title = "30天内书籍类别借阅统计"
        else:
            title = "全部时间书籍类别借阅统计"
        self.title_label = tk.Label(self.window, text=title, font=("SimHei", 16, "bold"))
        self.title_label.pack(pady=20)
        
        # 创建图表框架
        self.chart_frame = tk.Frame(self.window)
        self.chart_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
        
        # 创建表格框架
        self.table_frame = tk.Frame(self.window)
        self.table_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
        
        # 创建表格
        self.create_table()
        
        # 生成统计数据并显示
        self.generate_stats()
    
    def create_table(self):
        """创建表格"""
        columns = ("类别", "借阅次数", "占比")
        self.tree = ttk.Treeview(self.table_frame, columns=columns, show="headings")
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=150, anchor=tk.CENTER)
        
        scrollbar = ttk.Scrollbar(self.table_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.pack(fill=tk.BOTH, expand=True)
    
    def generate_stats(self):
        """生成统计数据并显示图表和表格"""
        # 计算类别统计
        category_counter = category_borrow_stats(self.records, self.books, self.time_range)
        
        # 准备图表数据
        categories = list(category_counter.keys())
        borrow_counts = list(category_counter.values())
        total = sum(borrow_counts)
        
        # 创建图表
        self.figure, self.ax = plt.subplots(figsize=(10, 6))
        
        # 计算百分比
        percentages = [count / total * 100 for count in borrow_counts]
        
        # 饼图颜色
        colors = plt.cm.tab20.colors
        
        # 绘制饼图
        wedges, texts, autotexts = self.ax.pie(
            borrow_counts,
            labels=categories,
            autopct=lambda p: f'{p:.1f}%',
            startangle=90,
            colors=colors,
            wedgeprops={'edgecolor': 'w', 'linewidth': 1}
        )
        
        # 设置字体大小
        plt.setp(autotexts, size=8)
        plt.setp(texts, size=10)
        
        # 设置标题
        if self.time_range:
            self.ax.set_title("30天内书籍类别借阅分布", fontsize=14)
        else:
            self.ax.set_title("全部时间书籍类别借阅分布", fontsize=14)
        
        # 使饼图为正圆形
        self.ax.axis('equal')
        
        # 将图表放置在Tkinter窗口中
        self.canvas = FigureCanvasTkAgg(self.figure, self.chart_frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)
        self.canvas.draw()
        
        # 清空表格
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        # 填充表格数据
        for category, count in category_counter.items():
            percentage = f"{count / total * 100:.1f}%"
            self.tree.insert("", tk.END, values=(category, count, percentage))
