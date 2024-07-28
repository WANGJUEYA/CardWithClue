package com.knowledge.graph.uitils.libs.music163;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.knowledge.graph.config.GraphConfig;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.music163.entity.Artist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Schema(description = "关注歌手")
@Component
public class CrawlerMusic163FavImpl extends AbstractCrawler {

    @Resource
    private GraphConfig graphConfig;

    @Override
    public List<DataGraph> crawler() {
        // 网易云该页面有 csrf_token 和 rsa 加密; 没有找到公钥也没有找到参数，没有办法处理爬虫；先本地存储音乐人吧
        // https://music.163.com/#/my/m/music/artist
        // https://music.163.com/weapi/artist/sublist
        String filePath = graphConfig.getProjPath() + "/db/music163artist.json";

        String json;
        try {
            json = CharStreams.fromFileName(filePath).toString();
        } catch (Exception e) {
            log.error("获取本地内容失败 >>> ", e);
            return new ArrayList<>();
        }
        List<Artist> local = JSON.parseArray(json, Artist.class);
        try {
            JSONObject years = JSONObject.parse(CharStreams.fromFileName(graphConfig.getProjPath() + "/db/music163artistYear.json").toString());
            if (years != null && !years.isEmpty()) {
                local.forEach(e -> e.setTime(years.getString(e.getName())));
            }
        } catch (Exception e) {
            log.error("获取歌手出生/成立时间失败 >>>> {}", e.getMessage());
        }
        return Optional.ofNullable(local).orElse(new ArrayList<>()).stream()
                .sorted(Comparator.comparingLong(e -> Long.parseLong(e.getId())))
                .map(Artist::graphItem).toList();
    }

}
