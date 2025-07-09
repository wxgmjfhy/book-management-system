import dateutil.parser as parser

def parse_date(date_str):
    """智能解析多种日期格式"""
    if not date_str or date_str.lower() in ['nan', 'none', 'null']:
        return None
    try:
        return parser.parse(date_str).date()
    except Exception as e:
        print(f"无法解析日期: {date_str}, 错误: {e}")
        return None
