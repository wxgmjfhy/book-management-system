package main.java.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * 控制台输入 工具类
 */
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 关闭扫描器, 释放资源
     */
    public static void close() {
        scanner.close();
    }

    /**
     * 辅助方法: 输出提示信息, 读取一行
     */
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * 读取字符串 (允许为空)
     * @param prompt 提示信息
     * @return 用户输入的字符串
     */
    public static String readString(String prompt) {
        return readLine(prompt);
    }

    /**
     * 读取整数 (不可为空)
     * @param prompt 提示信息
     * @return 用户输入的整数
     */
    public static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("输入无效, 请输入一个整数!");
            }
        }
    }

    /**
     * 读取整数 (允许为空)
     * @param prompt 提示信息
     * @return 用户输入的整数
     */
    public static Integer readInteger(String prompt) {
        String input = readLine(prompt).trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("输入无效, 请输入一个整数!");
            return readInteger(prompt);
        }
    }

    /**
     * 读取双精度浮点数 (允许为空)
     * @param prompt 提示信息
     * @return 用户输入的双精度浮点数
     */
    public static Double readDouble(String prompt) {
        String input = readLine(prompt);
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("输入无效, 请输入一个数字!");
            return readDouble(prompt);
        }
    }

    /**
     * 读取日期 (允许为空)
     * @param prompt 提示信息
     * @return 用户输入的日期
     */
    public static LocalDate readDate(String prompt) {
        String input = readLine(prompt);
        if (input.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("输入无效, 请输入一个日期!");
            return readDate(prompt);
        }
    }

    // 防止实例化
    private InputUtil() {}
}