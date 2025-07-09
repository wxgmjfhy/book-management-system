import tkinter as tk
from tkinter import ttk, messagebox
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import numpy as np
from collections import Counter
import csv
from due import almost_due , AlmostDueWindow
from datetime import datetime, timedelta
from dateutil import parser
from read import read_books, read_borrow_records
from statics import generate_book_ranking, generate_borrower_ranking
from category import category_borrow_stats, CategoryStatsWindow
# 设置 matplotlib 的字体以支持中文
plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置默认字体

# 设置中文字体
# plt.rcParams["font.family"] = ["SimHei", "WenQuanYi Micro Hei", "Heiti TC"]


class RankingApp:
    def __init__(self, root, books, records):
        self.root = root
        self.root.title("图书借阅排行榜")
        self.root.geometry("1000x600")
        
        self.books = books
        self.records = records
        
        # 创建主框架
        self.main_frame = tk.Frame(root)
        self.main_frame.pack(fill=tk.BOTH, expand=True)
        
        # 创建左侧按钮框架
        self.button_frame = tk.Frame(self.main_frame, width=200, bg="#f0f0f0")
        self.button_frame.pack(side=tk.LEFT, fill=tk.Y)
        
        # 创建右侧内容框架
        self.content_frame = tk.Frame(self.main_frame, bg="white")
        self.content_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)
        
        # 创建标题标签
        self.title_label = tk.Label(self.content_frame, text="请选择一个功能", font=("SimHei", 16, "bold"))
        self.title_label.pack(pady=20)
        
        # 创建表格框架
        self.table_frame = tk.Frame(self.content_frame)
        self.table_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
        
        # 创建图表框架
        self.chart_frame = tk.Frame(self.content_frame)
        self.chart_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
        
        # 创建按钮
        self.create_buttons()
        
        # 初始化表格
        self.create_table()
        
        # 初始化图表
        self.figure, self.ax = plt.subplots(figsize=(8, 5))
        self.canvas = FigureCanvasTkAgg(self.figure, self.chart_frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)
    
    def create_buttons(self):
        """创建左侧按钮"""
        buttons = [
            {"text": "最受欢迎的图书（全部时间）", "command": lambda: self.show_ranking("book_all")},
            {"text": "最受欢迎的图书（最近30天）", "command": lambda: self.show_ranking("book_30d")},
            {"text": "最活跃的借阅者（全部时间）", "command": lambda: self.show_ranking("borrower_all")},
            {"text": "最活跃的借阅者（最近90天）", "command": lambda: self.show_ranking("borrower_90d")},
            {"text": "30天书籍类别统计", "command": lambda: self.function2(30)},
            {"text": "全部时间书籍类别统计", "command": lambda: self.function2(None)},
            {"text": "展示即将逾期用户", "command": self.function1}
        ]
        
        for button_info in buttons:
            btn = tk.Button(
                self.button_frame,
                text=button_info["text"],
                font=("SimHei", 10),
                bg="#e0e0e0",
                relief=tk.FLAT,
                padx=10,
                pady=10,
                command=button_info["command"]
            )
            btn.pack(fill=tk.X, padx=10, pady=5, anchor=tk.W)
    
    def create_table(self):
        """创建表格"""
        columns = ("排名", "ID", "名称", "作者/借阅者ID", "借阅次数")
        self.tree = ttk.Treeview(self.table_frame, columns=columns, show="headings")
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=100, anchor=tk.CENTER)
        
        self.tree.column("名称", width=300)
        self.tree.column("作者/借阅者ID", width=200)
        
        scrollbar = ttk.Scrollbar(self.table_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.pack(fill=tk.BOTH, expand=True)
    
    def show_ranking(self, ranking_type):
        """显示排行榜"""
        # 清空表格
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        # 清空图表
        self.ax.clear()
        
        if ranking_type == "book_all":
            self.title_label.config(text="最受欢迎的图书（全部时间）")
            ranking = generate_book_ranking(self.records, self.books, top_n=10)
            self.populate_book_table(ranking)
            self.plot_book_chart(ranking, "全部时间")
            
        elif ranking_type == "book_30d":
            self.title_label.config(text="最受欢迎的图书（最近30天）")
            ranking = generate_book_ranking(self.records, self.books, top_n=10, time_range=30)
            self.populate_book_table(ranking)
            self.plot_book_chart(ranking, "最近30天")
            
        elif ranking_type == "borrower_all":
            self.title_label.config(text="最活跃的借阅者（全部时间）")
            ranking = generate_borrower_ranking(self.records, top_n=10)
            self.populate_borrower_table(ranking)
            self.plot_borrower_chart(ranking, "全部时间")
            
        elif ranking_type == "borrower_90d":
            self.title_label.config(text="最活跃的借阅者（最近90天）")
            ranking = generate_borrower_ranking(self.records, top_n=10, time_range=90)
            self.populate_borrower_table(ranking)
            self.plot_borrower_chart(ranking, "最近90天")
        
        # 更新图表
        self.figure.tight_layout()
        self.canvas.draw()
    
    def populate_book_table(self, ranking):
        """填充图书排行榜表格"""
        for item in ranking:
            self.tree.insert("", tk.END, values=(
                item["rank"],
                item["book_name"],
                # item["title"],
                item["author"],
                item["borrow_count"]
            ))
    
    def populate_borrower_table(self, ranking):
        """填充借阅者排行榜表格"""
        for item in ranking:
            self.tree.insert("", tk.END, values=(
                item["rank"],
                item["borrower_id"],
                "借阅者 #" + str(item["borrower_id"]),
                "",
                item["borrow_count"]
            ))
    
    def plot_book_chart(self, ranking, time_period):
        """绘制图书排行榜图表"""
        titles = [item["book_name"][:15] + "..." if len(item["book_name"]) > 15 else item["book_name"] for item in ranking]
        counts = [item["borrow_count"] for item in ranking]
        
        self.ax.barh(titles, counts, color='#4CAF50')
        self.ax.set_xlabel('借阅次数')
        self.ax.set_title(f'最受欢迎的图书 ({time_period})')
        self.ax.invert_yaxis()  # 让排名高的图书在上方
        
        # 添加数值标签
        for i, v in enumerate(counts):
            self.ax.text(v + 0.5, i, str(v), va='center')
    
    def plot_borrower_chart(self, ranking, time_period):
        """绘制借阅者排行榜图表"""
        borrowers = [f"#{item['borrower_id']}" for item in ranking]
        counts = [item["borrow_count"] for item in ranking]
        
        self.ax.barh(borrowers, counts, color='#2196F3')
        self.ax.set_xlabel('借阅次数')
        self.ax.set_title(f'最活跃的借阅者 ({time_period})')
        self.ax.invert_yaxis()  # 让排名高的借阅者在上方
        
        # 添加数值标签
        for i, v in enumerate(counts):
            self.ax.text(v + 0.5, i, str(v), va='center')
    
    def function1(self):
        """显示即将逾期的借阅记录"""
        AlmostDueWindow(self.root, self.books, self.records)
    
    def function2(self  ,time_range=None):
        """示例功能函数2"""
        CategoryStatsWindow(self.root, self.books, self.records, time_range)

def main():
    # 读取数据
    try:
        books = read_books('./src/main/resources/books.csv')
        records = read_borrow_records('./src/main/resources/records.csv')
        print(f"已读取 {len(books)} 本图书和 {len(records)} 条借阅记录")
    except Exception as e:
        print(f"读取数据失败: {e}")
        return
    
    # 创建GUI应用
    root = tk.Tk()
    app = RankingApp(root, books, records)
    root.mainloop()

if __name__ == "__main__":
    main()