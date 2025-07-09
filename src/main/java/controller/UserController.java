package main.java.controller;

import main.java.entity.User;
import main.java.enums.UserRole;
import main.java.service.UserService;
import main.java.util.InputUtil;

public class UserController {

    private User user = null;
    private final UserService userService = new UserService();

    public User getUser() {
        return user;
    }

    public boolean isLogin() {
        return user != null;
    }

    public void register() {
        String username = InputUtil.readString("请输入账户名:");
        String name = InputUtil.readString("请输入姓名:");
        String password = InputUtil.readString("请输入密码:");
        String email = InputUtil.readString("请输入邮箱:");
        UserRole role = UserRole.STUDENT; // 只允许注册为学生

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole(role);

        String message = userService.register(newUser);
        System.out.println(message);
    }

    public void login() {
        System.out.println("请选择登录方式: 1. 账户名登录, 2. 邮箱登录");
        int choice = InputUtil.readInt("请输入选择:");
        if (choice == 1) {
            loginByUsername();
        } else if (choice == 2) {
            loginByEmail();
        } else {
            System.out.println("无效选择, 取消登录");
        }
    }

    private void loginByUsername() {
        String username = InputUtil.readString("请输入账户名:");
        String password = InputUtil.readString("请输入密码:");
        user = userService.loginByUsername(username, password);
        if (user == null) {
            System.out.println("登录失败");
        } else {
            System.out.println("登录成功");
        }
    }

    private void loginByEmail() {
        String email = InputUtil.readString("请输入邮箱:");
        String password = InputUtil.readString("请输入密码:");
        user = userService.loginByEmail(email, password);
        if (user == null) {
            System.out.println("登录失败");
        } else {
            System.out.println("登录成功");
        }
    }

    public void logout() {
        user = null;
        System.out.println("-- 退出登录 --");
    }

}
