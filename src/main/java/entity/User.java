package main.java.entity;

import main.java.enums.UserRole;

/**
 * 用户 实体类
 */
public class User {
    /**
     * id
     */
    private Integer id;

    /**
     * 账户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 身份
     */
    private UserRole role;

    /**
     * 邮箱
     */
    private String email;

    public User() {
    }

    public User(Integer id, String username, String name, String password, UserRole role, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStudent() {
        return UserRole.STUDENT.equals(this.role);
    }

    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }
}
