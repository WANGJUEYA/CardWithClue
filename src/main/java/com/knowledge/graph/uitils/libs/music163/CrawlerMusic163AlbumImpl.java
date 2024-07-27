package com.knowledge.graph.uitils.libs.music163;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Schema(description = "专辑")
public class CrawlerMusic163AlbumImpl extends AbstractCrawler {

    @Schema(description = "歌手卡片")
    @Getter
    @Setter
    private DataCard singer;

    public CrawlerMusic163AlbumImpl(DataCard singer) {
        this.singer = singer;
    }

    @Override
    public List<DataGraph> crawler() {
//        String filePath = graphConfig.getProjPath() + "/db/music163artist.json";
//        String json;
//        try {
//            json = CharStreams.fromFileName(filePath).toString();
//        } catch (Exception e) {
//            log.error("获取本地内容失败 >>> ", e);
//            return new ArrayList<>();
//        }
//        List<Artist> local = JSON.parseArray(json, Artist.class);
//        return Optional.ofNullable(local).orElse(new ArrayList<>()).stream().map(a -> a.graphItem(SOURCE)).toList();
        return new ArrayList<>();
    }

}
