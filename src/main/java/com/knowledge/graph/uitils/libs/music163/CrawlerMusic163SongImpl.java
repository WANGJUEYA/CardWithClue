package com.knowledge.graph.uitils.libs.music163;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.music163.entity.Album;
import com.knowledge.graph.uitils.libs.music163.entity.Artist;
import com.knowledge.graph.uitils.libs.music163.entity.Song;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.knowledge.graph.uitils.libs.music163.LibsConstant.*;

@Getter
@Slf4j
@Schema(description = "歌曲")
public class CrawlerMusic163SongImpl extends AbstractCrawler {

    private final Artist artist;

    private final Album album;

    private final List<Song> songs = new ArrayList<>();

    public CrawlerMusic163SongImpl(Artist artist, Album album) {
        this.artist = artist;
        this.album = album;
    }

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml(REQUEST_ALBUM_PRE + album.getId(), HEADER_KEY, HEADER_VALUE);
        Elements doc;
        // 处理歌曲数据
        if (html == null || (doc = html.getElementById("song-list-pre-cache").getElementsByTag("a")).isEmpty()) {
            return new ArrayList<>();
        }

        for (Element element : doc) {
            try {
                String musicHref = element.attributes().get("href");
                String musicId = musicHref.split("id=")[1];
                String name = element.text();

                songs.add(new Song(musicId, name));
            } catch (Exception e) {
                log.error("数据获取失败");
            }
        }

        return songs.stream().map(a -> a.graphItem(artist.createCard(), album.createCard())).toList();
    }

}
