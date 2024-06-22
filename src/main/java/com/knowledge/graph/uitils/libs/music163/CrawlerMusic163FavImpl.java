package com.knowledge.graph.uitils.libs.music163;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardKeyEnum;
import com.knowledge.graph.common.entity.DataGraphItem;
import com.knowledge.graph.config.GraphConfig;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.music163.entity.Artist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.knowledge.graph.common.constant.CardGroupEnum.THING_PERSON;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_MUSIC_163_FAV;

@Slf4j
@Schema(description = "关注歌手")
@Component
public class CrawlerMusic163FavImpl extends AbstractCrawler {

    @Resource
    private GraphConfig graphConfig;

    private static final DataCard SOURCE = CardKeyEnum.MUSIC_163.card();

    @Override
    public DataCard getSource() {
        return SOURCE;
    }

    @Override
    public void setSource(DataCard source) {
    }

    @Override
    public Wrapper<DataCard> wrapperCard() {
        return Wrappers.lambdaQuery(DataCard.class).eq(DataCard::getCardGroup, THING_PERSON);
    }

    @Override
    public Wrapper<DataClue> wrapperClue() {
        return Wrappers.lambdaQuery(DataClue.class).eq(DataClue::getClueGroup, LIB_MUSIC_163_FAV);
    }

    @Override
    public List<DataGraphItem> crawler() {
        String filePath = graphConfig.getProjPath() + "/db/music163artist.json";
        String json;
        try {
            json = CharStreams.fromFileName(filePath).toString();
        } catch (Exception e) {
            log.error("获取本地内容失败 >>> ", e);
            return new ArrayList<>();
        }
        List<Artist> local = JSON.parseArray(json, Artist.class);
        return Optional.ofNullable(local).orElse(new ArrayList<>()).stream().map(a -> a.graphItem(SOURCE)).toList();
    }

}
