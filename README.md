# 项目架构

```plaintext
src/
├── main/
│   ├── java/
│   │   ├── controller/          // 表现层
│   │   │   ├── BookController.java
│   │   │   └── UserController.java
│   │   ├── dao/                 // 数据访问层
│   │   │   ├── BookDao.java
│   │   │   ├── RecordDao.java
│   │   │   └── UserDao.java
│   │   ├── dto/                 // 数据传输对象
│   │   │   ├── BookQueryParams.java
│   │   │   └── RecordDTO.java
│   │   ├── entity/              // 实体类
│   │   │   ├── Book.java
│   │   │   ├── Record.java
│   │   │   └── User.java
│   │   ├── enums/               // 枚举类
│   │   │   ├── BookCategory.java
│   │   │   ├── BookLanguage.java
│   │   │   ├── BookStatus.java
│   │   │   ├── RecordStatus.java
│   │   │   └── UserRole.java
│   │   ├── service/             // 业务逻辑层
│   │   │   ├── BookService.java
│   │   │   ├── RecordService.java
│   │   │   └── UserService.java
│   │   ├── util/                // 工具类
│   │   │   ├── FileUtil.java
│   │   │   └── InputUtil.java
│   │   └── Main.java            // 程序入口
│   └── resources/               // 资源文件
│       ├── books.csv
│       ├── records.csv
│       └── users.csv
└── test/                        // 测试代码
```