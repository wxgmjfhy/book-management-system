package main.java.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * CSV 文件操作 工具类
 */
public class FileUtil {

    // 基础路径
    private static final String BASE_PATH = "src/main/resources/";

    /**
     * 读取 CSV 文件并转换为实体列表
     * @param fileName 文件名
     * @param mapper 字符串行转实体的映射函数
     * @return 实体列表
     */
    public static <T> List<T> readCsv(String fileName, Function<String, T> mapper) {
        List<T> list = new ArrayList<>();
        String path = BASE_PATH + fileName;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            // 跳过表头
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T entity = mapper.apply(line);
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件失败: " + fileName + "，错误: " + e.getMessage());
        }
        return list;
    }

    /**
     * 写入实体列表到 CSV 文件 (覆盖原文件)
     * @param fileName 文件名
     * @param header 表头行
     * @param entities 实体列表
     * @param toStringFunc 实体转 CSV 行的函数
     * @return 是否成功
     */
    public static <T> boolean writeCsv(String fileName, String header, List<T> entities, Function<T, String> toStringFunc) {
        String path = BASE_PATH + fileName;
        List<String> lines = new ArrayList<>();
        lines.add(header); // 添加表头

        // 转换实体为行
        for (T entity : entities) {
            lines.add(toStringFunc.apply(entity));
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            // 仅在每行之间添加换行符，避免最后一行多一个空行
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                // 最后一行不添加换行符
                if (i != lines.size() - 1) {
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("写入文件失败: " + fileName + "，错误: " + e.getMessage());
            return false;
        }
    }

    /**
     * 新增实体到 CSV 文件 (追加到末尾)
     * @param fileName 文件名
     * @param entity 实体
     * @param toStringFunc 实体转 CSV 行的函数
     * @return 是否成功
     */
    public static <T> boolean appendCsv(String fileName, T entity, Function<T, String> toStringFunc) {
        String path = BASE_PATH + fileName;
        String line = toStringFunc.apply(entity);

        // 检查文件是否存在且非空，决定是否需要先添加换行符
        boolean needsNewLine = false;
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            needsNewLine = true;
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8))) {
            // 如果文件已有内容，先添加换行符
            if (needsNewLine) {
                bw.newLine();
            }
            bw.write(line);
            return true;
        } catch (IOException e) {
            System.err.println("追加文件失败: " + fileName + ", 错误: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取下一个自增 ID (基于现有最大 ID + 1)
     * @param fileName 文件名
     * @param idGetter 从行字符串中提取 ID 的函数
     * @return 下一个 ID
     */
    public static int getNextId(String fileName, Function<String, Integer> idGetter) {
        int maxId = 0;
        String path = BASE_PATH + fileName;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // 跳过表头
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Integer id = idGetter.apply(line);
                    if (id != null && id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("获取自增 ID 失败: " + fileName + "，错误: " + e.getMessage());
        }
        return maxId + 1;
    }
}