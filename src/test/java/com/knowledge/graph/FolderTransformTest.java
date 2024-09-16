package com.knowledge.graph;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.tag.FieldKey;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FolderTransformTest {

    @Test
    void contextLoads() throws IOException {
        List<String> errorPath = new ArrayList<>();
        long count = Files.walk(Path.of("D:\\共享数据\\共享音乐-重写\\@[1974]陈奕迅\\"))
                .filter(path -> path.toString().toLowerCase().endsWith(".mp3") || path.toString().toLowerCase().endsWith(".flac"))
                .peek(System.out::println)
                .mapToInt(path -> {
                    try {
                        if (!path.toString().contains("共享音乐-重写")) {
                            String fileName = path.getFileName().toString();
                            String songName = fileName;
                            if (songName.contains("陈奕迅") || songName.contains("陳奕迅")) {
                                String[] split = fileName.split("-");
                                songName = split[1].substring(0, split[1].length() - 4).trim();
                            }
                            String parentPath = path.getParent().getFileName().toString();
                            if (!parentPath.contains("-")) {
                                parentPath = path.getParent().getParent().getFileName().toString();
                            }
                            String[] splitParent = parentPath.split("-");
                            String albumName = splitParent[1];
                            String albumYear = splitParent[0];
                            System.out.println("----------------------");
                            System.out.println(songName);
                            System.out.println(albumName);
                            System.out.println(albumYear);
                            System.out.println("----------------------");
                            commandExe(path, songName, "陈奕迅", albumName, albumYear);
                        }
                    } catch (Exception e) {
                        errorPath.add(path.toString());
                        log.error("写入标签失败", e);
                    }
                    return 1;
                }).sum();
        log.info("总条数 >>> {}", count);
        log.info("失败数据 >>> {}", errorPath);
        log.info("手动执行脚本 >>> {}", commands);
    }

    List<String> commands = new ArrayList<>();

    /**
     * @param oldPath    原mp3路径
     * @param songName   title
     * @param artistName artist
     * @param albumName  album
     * @param albumYear  year
     */
    void commandExe(Path oldPath, String songName, String artistName, String albumName, String albumYear) throws IOException, InterruptedException {
        String newPath = oldPath.toString().replace("共享音乐", "共享音乐-重写");
        newPath = newPath.replace("陳奕迅", "陈奕迅")
                .replace("陳奕迅 - ", "陈奕迅-")
                .replace("陈奕迅 - ", "陈奕迅-")
                .replace("陈奕迅 _ ", "陈奕迅-")
                .replace("陳奕迅 -", "陈奕迅-")
                .replace("陈奕迅 -", "陈奕迅-")
                .replace("陈奕迅_", "陈奕迅-");
        Path destination = Path.of(newPath);
        if (!Files.exists(destination.getParent())) {
            Files.createDirectories(destination.getParent());
        }

        // -c copy 表示不更改文件编码，进入这里表示mp3格式不规范, 我们就把他改了吧
        String command = "ffmpeg -i \"" + oldPath + "\"";

        String metadata = " -metadata %s=\"%s\" ";
        if (StringUtils.isNotBlank(songName)) {
            command += String.format(metadata, FieldKey.TITLE, songName);
        }
        if (StringUtils.isNotBlank(artistName)) {
            command += String.format(metadata, FieldKey.ARTIST, artistName);
        }
        if (StringUtils.isNotBlank(albumName)) {
            command += String.format(metadata, FieldKey.ALBUM, albumName);
        }
        if (StringUtils.isNotBlank(albumYear)) {
            command += String.format(metadata, FieldKey.YEAR, albumYear.substring(0, 4));
        }
        // -y是强行覆盖
        command += "\"" + newPath + "\" -y\n";
        System.out.println(command);
        commands.add(command);

        if (false) {
            // 执行命令
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);

            // 读取命令执行结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成 // 这句话会一直等待
            process.waitFor(10, TimeUnit.SECONDS);
            if (process.isAlive()) {
                // 如果超时后进程仍然存活，可以选择强制终止
                process.destroyForcibly();
            }
            // 获取退出值
            int exitValue = process.exitValue();
            System.out.println("Exit value: " + exitValue);
        }
    }

}
