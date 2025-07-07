# 项目架构

```plaintext
src/
├── main/
│   ├── java/
│   │   ├── entity/              // 实体类
│   │   │   ├── User.java
│   │   │   ├── Book.java
│   │   │   └── Record.java
│   │   ├── dao/                 // 数据访问层
│   │   │   ├── UserDao.java
│   │   │   ├── BookDao.java
│   │   │   └── RecordDao.java
│   │   ├── service/             // 业务逻辑层
│   │   │   ├── UserService.java
│   │   │   ├── BookService.java
│   │   │   └── RecordService.java
│   │   ├── controller/          // 表现层
│   │   │   ├── UserController.java
│   │   │   ├── BookController.java
│   │   │   └── RecordController.java
│   │   ├── util/                // 工具类
│   │   │   └── FileUtil.java
│   │   └── Main.java            // 程序入口
│   └── resources/               // 资源文件
│       ├── users.csv
│       ├── books.csv
│       └── records.csv
└── test/                        // 测试代码
```