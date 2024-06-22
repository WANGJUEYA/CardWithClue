package com.knowledge.graph.uitils.libs.music163;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
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
    public Wrapper<DataCard> wrapperCard() {
        return Wrappers.lambdaQuery(DataCard.class).eq(DataCard::getCardGroup, CardGroupEnum.COLLECT_ALBUM);
    }

    @Override
    public Wrapper<DataClue> wrapperClue() {
        return Wrappers.lambdaQuery(DataClue.class)
                .eq(DataClue::getClueGroup, ClueGroupEnum.LIB_STORE_MUSIC_163)
                .or().and(j -> j.eq(DataClue::getSource, singer.getId()).eq(DataClue::getClueGroup, ClueGroupEnum.PUBIC_ALBUM));
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
