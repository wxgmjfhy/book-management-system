package main.java.dao;

import main.java.entity.User;
import main.java.enums.UserRole;
import main.java.util.FileUtil;
import java.util.List;

public class UserDao {
    // CSV 文件路径和表头
    private static final String USERS_CSV = "users.csv";
    private static final String USER_HEADER = "id,username,name,password,role,email";

    /**
     * 查询所有用户
     */
    public List<User> listAll() {
        // 调用 FileUtil 读取 CSV 并映射为 User 实体
        return FileUtil.readCsv(USERS_CSV, line -> {
            String[] parts = line.split(",", -1); // -1 保留空字段
            if (parts.length != 6) {
                System.err.println("用户数据格式错误: " + line);
                return null;
            }

            User user = new User();
            try {
                user.setId(parts[0].isEmpty() ? null : Integer.parseInt(parts[0]));
                user.setUsername(parts[1].isEmpty() ? null : parts[1]);
                user.setName(parts[2].isEmpty() ? null : parts[2]);
                user.setPassword(parts[3].isEmpty() ? null : parts[3]);
                user.setRole(parts[4].isEmpty() ? null : UserRole.valueOf(parts[4]));
                user.setEmail(parts[5].isEmpty() ? null : parts[5]);
            } catch (IllegalArgumentException e) {
                System.err.println("用户角色解析失败: " + parts[4] + "，错误: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("解析用户数据失败: " + line + "，错误: " + e.getMessage());
                return null;
            }
            return user;
        });
    }

    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        List<User> users = listAll();
        return users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据邮箱查询用户
     */
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        List<User> users = listAll();
        return users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 新增用户
     */
    public void save(User user) {
        // 生成自增 ID
        int newId = FileUtil.getNextId(USERS_CSV, line -> {
            String[] parts = line.split(",");
            return parts.length > 0 && !parts[0].isEmpty() ? Integer.parseInt(parts[0]) : 0;
        });
        user.setId(newId);

        // 追加到 CSV 文件
        FileUtil.appendCsv(USERS_CSV, user, this::toCsvLine);
    }

    /**
     * 将 User 实体转换为 CSV 行字符串
     */
    private String toCsvLine(User user) {
        return String.join(",",
                // 处理 null 值为空字符串
                user.getId() == null ? "" : user.getId().toString(),
                user.getUsername() == null ? "" : user.getUsername(),
                user.getName() == null ? "" : user.getName(),
                user.getPassword() == null ? "" : user.getPassword(),
                user.getRole() == null ? "" : user.getRole().name(),
                user.getEmail() == null ? "" : user.getEmail()
        );
    }
}