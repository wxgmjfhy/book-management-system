package main.java.service;

import main.java.dao.UserDao;
import main.java.entity.User;
import main.java.enums.UserRole;
import java.util.regex.Pattern;

public class UserService {

    private final UserDao userDao = new UserDao();

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public User loginByUsername(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            return null;
        }

        User user = userDao.findByUsername(username.trim());
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    public User loginByEmail(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null) {
            return null;
        }

        User user = userDao.findByEmail(email.trim());
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    public String register(User newUser) {
        if (newUser == null) {
            return "注册失败：用户信息不能为空";
        }

        // 验证用户名
        String username = newUser.getUsername();
        if (username == null || username.trim().isEmpty()) {
            return "注册失败：用户名不能为空";
        }
        username = username.trim();
        if (username.length() < 3 || username.length() > 20) {
            return "注册失败：用户名长度必须在3-20个字符之间";
        }
        if (userDao.findByUsername(username) != null) {
            return "注册失败：用户名已存在";
        }

        // 验证密码
        String password = newUser.getPassword();
        if (password == null || password.length() < 6) {
            return "注册失败：密码长度不能少于6位";
        }

        // 验证邮箱
        String email = newUser.getEmail();
        if (email == null || email.trim().isEmpty()) {
            return "注册失败：邮箱不能为空";
        }
        email = email.trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "注册失败：邮箱格式不正确";
        }
        if (userDao.findByEmail(email) != null) {
            return "注册失败：邮箱已被注册";
        }

        // 验证姓名
        String name = newUser.getName();
        if (name == null || name.trim().isEmpty()) {
            return "注册失败：姓名不能为空";
        }

        // 设置默认角色
        if (newUser.getRole() == null) {
            newUser.setRole(UserRole.STUDENT);
        }

        // 保存用户
        newUser.setUsername(username);
        newUser.setEmail(email);

        try {
            userDao.save(newUser);
            return "注册成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "注册失败：服务器错误";
        }
    }
}