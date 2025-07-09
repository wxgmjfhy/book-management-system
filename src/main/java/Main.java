package main.java;

import main.java.controller.BookController;
import main.java.controller.UserController;
import main.java.entity.User;
import main.java.util.InputUtil;

/**
 * 程序入口
 */
public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        BookController bookController = new BookController();

        while (true) {
            if (!userController.isLogin()) {
                System.out.println("-- 校园图书借阅助手 --");
                System.out.println("1. 注册");
                System.out.println("2. 登录");
                System.out.println("3. 退出");

                int choice = InputUtil.readInt("请输入选择:");

                switch (choice) {
                    case 1:
                        userController.register();
                        break;
                    case 2:
                        userController.login();
                        break;
                    case 3:
                        System.out.println("感谢使用校园图书借阅助手, 再见!");
                        InputUtil.close();
                        return;
                    default:
                        System.out.println("无效选择, 请重新输入!");
                }
            } else {
                User user = userController.getUser();
                System.out.println("欢迎使用校园图书借助助手, " + user.getUsername() + " (" + user.getRole() + ")");
                System.out.println("1. 开始菜单");
                System.out.println("2. 退出登录");

                int choice = InputUtil.readInt("请输入选择:");

                switch (choice) {
                    case 1:
                        bookController.bookOperations(user);
                        break;
                    case 2:
                        userController.logout();
                        break;
                    default:
                        System.out.println("无效选择, 请重新输入!");
                }
            }
        }
    }
}
