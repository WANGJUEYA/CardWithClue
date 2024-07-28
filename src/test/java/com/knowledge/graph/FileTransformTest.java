package com.knowledge.graph;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                .in(DataCard::getDataGroup, CardGroupEnum.THING_PERSON, CardGroupEnum.COLLECT_ALBUM, CardGroupEnum.THING_MUSIC));
        List<DataClue> dataClues = dataClueService.list(Wrappers.lambdaQuery(DataClue.class)
                .in(DataClue::getDataGroup, ClueGroupEnum.SINGER, ClueGroupEnum.COLLECT_ALBUM));
        Map<String, String> nameMap = dataCards.stream().collect(Collectors.toMap(DataCard::getKey, DataCard::getId, (o, v) -> o));
        Map<String, DataCard> idMap = dataCards.stream().collect(Collectors.toMap(DataCard::getId, Function.identity(), (o, v) -> o));
        Map<String, String> songMap = dataClues.stream().collect(Collectors.toMap(e -> e.getDataGroup() + e.getTarget(), DataClue::getSource, (o, v) -> o));

        Files.walk(Path.of("D:\\共享数据\\共享音乐\\"))
                .filter(path -> path.toString().toLowerCase().endsWith(".mp3") || path.toString().toLowerCase().endsWith(".flac"))
                .peek(System.out::println)
                .forEach(path -> {
                    try {
                        // 加载MP3文件
                        MP3File mp3File = new MP3File(path.toFile());
                        Tag tag = mp3File.getID3v2TagAsv24();
                        if (!mp3File.hasID3v2Tag()) {
                            String fileName = path.getFileName().toString();
                            String[] split = fileName.split("-");
                            String songName = split[1].substring(0, split[1].length() - 4);
                            String artistName = split[0];
                            String albumName = null;
                            String albumYear = null;
                            // 找到专辑
                            if (nameMap.containsKey(songName)) {
                                String songId = nameMap.get(songName);
                                String clueSong = ClueGroupEnum.COLLECT_ALBUM + songId;
                                if (songMap.containsKey(clueSong)) {
                                    String albumId = songMap.get(clueSong);
                                    if (idMap.containsKey(albumId)) {
                                        DataCard album = idMap.get(albumId);
                                        albumName = album.getKey();
                                        albumYear = album.getTime();
                                    }
                                }
                            }
                            System.out.println(songName);
                            System.out.println(artistName);
                            System.out.println(albumName);
                            System.out.println(albumYear);

                            tag = new ID3v24Tag();
                            mp3File.setTag(tag);
                            // 设置唱片集信息
                            if (StringUtils.isNotBlank(albumName)) {
                                tag.setField(FieldKey.ALBUM, albumName);
                            }
                            tag.setField(FieldKey.ARTIST, artistName);
                            // tag.setField(FieldKey.ALBUM_ARTIST, "Album Artist Name");
                            tag.setField(FieldKey.TITLE, songName);
                            // tag.setField(FieldKey.TRACK, "2"); // 歌曲在专辑中的编号
                            // tag.setField(FieldKey.GENRE, "Genre");
                            if (StringUtils.isNotBlank(albumYear)) {
                                tag.setField(FieldKey.YEAR, albumYear);
                            }
                            // 保存更改
                            mp3File.commit();
                            System.out.println("标签已成功写入！");
                        } else {
                            // 读取并打印标签信息
                            System.out.println("艺术家: " + tag.getFirst(FieldKey.ARTIST));
                            System.out.println("专辑: " + tag.getFirst(FieldKey.ALBUM));
                            System.out.println("专辑艺术家: " + tag.getFirst(FieldKey.ALBUM_ARTIST));
                            System.out.println("标题: " + tag.getFirst(FieldKey.TITLE));
                            System.out.println("音轨号: " + tag.getFirst(FieldKey.TRACK));
                            System.out.println("流派: " + tag.getFirst(FieldKey.GENRE));
                            System.out.println("年份: " + tag.getFirst(FieldKey.YEAR));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }

}
