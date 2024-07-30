package com.knowledge.graph;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
public class FileTransformTest {

    public static void main(String[] args) throws IOException {
        // Get-ChildItem -Force -Recurse | Select-Object Name, Length, LastWriteTime, FullName
        Files.walk(Path.of("Z:\\"))
                .filter(Files::isRegularFile)
                .filter(path -> !path.toString().endsWith(".bk"))
                .filter(path -> path.getFileName().toString().contains("."))
                .forEach(path -> {
                    String pathStr = path.toString();
                    String fileNameStr = path.getFileName().toString();
                    String artist = pathStr.substring(pathStr.indexOf("-") + 1, pathStr.indexOf("\\", 3));
                    String fileNameNew = "D:\\共享数据\\共享音乐\\" + artist + "\\" + artist + "-" + fileNameStr;
                    System.out.println(fileNameNew);
                    try {
                        Path destination = Path.of(fileNameNew);
                        if (!Files.exists(destination.getParent())) {
                            Files.createDirectories(destination.getParent());
                        }
                        Files.copy(path, destination);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Resource
    IDataCardService dataCardService;
    @Resource
    IDataClueService dataClueService;

    @Test
    void contextLoads() throws IOException {
        List<DataCard> dataCards = dataCardService.list(Wrappers.lambdaQuery(DataCard.class)
                .in(DataCard::getDataGroup, CardGroupEnum.THING_PERSON, CardGroupEnum.THING_ALBUM, CardGroupEnum.THING_MUSIC));
        List<DataClue> dataClues = dataClueService.list(Wrappers.lambdaQuery(DataClue.class)
                .in(DataClue::getDataGroup, ClueGroupEnum.SINGER, ClueGroupEnum.COLLECT_ALBUM));
        Map<String, String> nameMap = dataCards.stream().collect(Collectors.toMap(e -> e.getDataGroup() + e.getKey(), DataCard::getId, (o, v) -> o));
        Map<String, DataCard> idMap = dataCards.stream().collect(Collectors.toMap(DataCard::getId, Function.identity(), (o, v) -> o));
        Map<String, String> songMap = dataClues.stream().collect(Collectors.toMap(e -> e.getDataGroup() + e.getTarget(), DataClue::getSource, (o, v) -> o));

        List<String> errorPath = new ArrayList<>();
        Files.walk(Path.of("D:\\共享数据\\共享音乐\\"))
                .filter(path -> path.toString().toLowerCase().endsWith(".mp3") || path.toString().toLowerCase().endsWith(".flac"))
                .peek(System.out::println)
                .forEach(path -> {
                    try {
                        String fileName = path.getFileName().toString();
                        String[] split = fileName.split("-");
                        String songName = split[1].substring(0, split[1].length() - 4);
                        String artistName = split[0];
                        String albumName = null;
                        String albumYear = null;
                        // 找到专辑
                        if (nameMap.containsKey(CardGroupEnum.THING_MUSIC + songName)) {
                            String songId = nameMap.get(CardGroupEnum.THING_MUSIC + songName);
                            String clueSong = ClueGroupEnum.COLLECT_ALBUM + songId;
                            if (songMap.containsKey(clueSong)) {
                                String albumId = songMap.get(clueSong);
                                if (idMap.containsKey(albumId)) {
                                    DataCard album = idMap.get(albumId);
                                    albumName = album.getKey();
                                    albumYear = album.getTime();
                                } else {
                                    System.out.println("albumId >>> " + albumId);
                                }
                            } else {
                                System.out.println("clueSong >>> " + clueSong);
                            }
                        } else {
                            System.out.println("songName >>> " + songName);
                        }
                        System.out.println("----------------------");
                        System.out.println(songName);
                        System.out.println(artistName);
                        System.out.println(albumName);
                        System.out.println(albumYear);
                        System.out.println("----------------------");
                        String songNameOld = null;
                        String artistNameOld = null;
                        String albumNameOld = null;
                        String albumYearOld = null;

                        boolean command = true;
                        MP3AudioHeader header = null;
                        // 加载MP3文件
                        MP3File mp3File = null;

                        try {
                            mp3File = new MP3File(path.toFile());
                            // 检查是不是标准mp3格式, 不是用ffmpeg重写
                            header = mp3File.getMP3AudioHeader();
                        } catch (Exception e) {
                            log.error("不是标准格式 >>> {}", e.getMessage());
                        }
                        if (header != null) {
                            Tag tag = mp3File.getTagOrCreateAndSetDefault();
                            // 读取并打印标签信息
                            System.out.println("----------------------");
                            artistNameOld = tag.getFirst(FieldKey.ARTIST);
                            System.out.println("艺术家: " + artistNameOld);
                            albumNameOld = tag.getFirst(FieldKey.ALBUM);
                            System.out.println("专辑: " + albumNameOld);
                            System.out.println("专辑艺术家: " + tag.getFirst(FieldKey.ALBUM_ARTIST));
                            songNameOld = tag.getFirst(FieldKey.TITLE);
                            System.out.println("标题: " + songNameOld);
                            System.out.println("音轨号: " + tag.getFirst(FieldKey.TRACK));
                            System.out.println("流派: " + tag.getFirst(FieldKey.GENRE));
                            albumYearOld = tag.getFirst(FieldKey.YEAR);
                            System.out.println("年份: " + albumYearOld);
                            System.out.println("----------------------");

                            boolean change = false;
                            // 设置唱片集信息
                            if (StringUtils.isBlank(songNameOld) && StringUtils.isNotBlank(songName)) {
                                change = true;
                                tag.setField(FieldKey.TITLE, songName);
                            }
                            // 歌手
                            if (StringUtils.isBlank(artistNameOld) && StringUtils.isNotBlank(artistName)) {
                                change = true;
                                tag.setField(FieldKey.ARTIST, artistName);
                            }
                            // 唱片名
                            if (StringUtils.isBlank(albumNameOld) && StringUtils.isNotBlank(albumName)) {
                                change = true;
                                tag.setField(FieldKey.ALBUM, albumName);
                            }
                            // 年份
                            if (StringUtils.isBlank(albumYearOld) && StringUtils.isNotBlank(albumYear)) {
                                change = true;
                                tag.setField(FieldKey.YEAR, albumYear);
                            }
                            // tag.setField(FieldKey.ALBUM_ARTIST, "Album Artist Name");
                            // tag.setField(FieldKey.TRACK, "2"); // 歌曲在专辑中的编号
                            // tag.setField(FieldKey.GENRE, "Genre");
                            if (change) {
                                try {
                                    // 保存更改
                                    mp3File.commit();
                                    System.out.println("标签已成功写入！");
                                    command = false;
                                } catch (Exception e) {
                                    log.error("写入标签失败, 尝试写入命令");
                                }
                            } else {
                                command = false;
                            }
                        }
                        if (command) {
                            commandExe(path,
                                    StringUtils.defaultIfBlank(songNameOld, songName),
                                    StringUtils.defaultIfBlank(artistNameOld, artistName),
                                    StringUtils.defaultIfBlank(albumNameOld, albumName),
                                    StringUtils.defaultIfBlank(albumYearOld, albumYear)
                            );
                        }
                    } catch (Exception e) {
                        errorPath.add(path.toString());
                        log.error("写入标签失败 >>> {}", e.getMessage());
                    }
                });
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
