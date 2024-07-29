package com.knowledge.graph.uitils.libs.music163;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.music163.entity.Album;
import com.knowledge.graph.uitils.libs.music163.entity.Artist;
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
@Schema(description = "专辑")
public class CrawlerMusic163AlbumImpl extends AbstractCrawler {

    private final Artist artist;

    private final List<Album> albums = new ArrayList<>();

    public CrawlerMusic163AlbumImpl(Artist artist) {
        this.artist = artist;
    }

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml(String.format(REQUEST_ALBUM_LIST_FORMAT, artist.getId()), HEADER_KEY, HEADER_VALUE);
        Elements doc;
        // 处理歌曲数据
        if (html == null || (doc = html.getElementsByAttributeValueStarting("data-id", artist.getId())).isEmpty()) {
            return new ArrayList<>();
        }

        for (Element element : doc.get(0).children()) {
            try {
                String date = element.getElementsByClass("s-fc3").text();
                Elements album = element.getElementsByClass("tit s-fc0");
                String name = album.text();
                String href = album.get(0).attributes().get("href");
                String albumId = href.split("id=")[1];

                // 同名的专辑存在可能性: 带上艺术家名字
                albums.add(new Album(albumId, artist.getName(), name, date));
            } catch (Exception e) {
                log.error("数据获取失败");
            }
        }
        return albums.stream().map(a -> a.graphItem(artist.createCard())).toList();
    }

}
