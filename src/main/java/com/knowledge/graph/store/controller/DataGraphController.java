package com.knowledge.graph.store.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcFavImpl;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcNovelImpl;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Author;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163FavImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.knowledge.graph.common.constant.CardGroupEnum.THING_PERSON;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;

@RestController
@RequestMapping("/store/graph")
public class DataGraphController {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;
    @Resource
    private IDataGraphService dataGraphService;
    @Resource
    private CrawlerMusic163FavImpl crawlerMusic163Fav;
    @Resource
    private CrawlerJjwxcFavImpl crawlerJjwxcFav;

    @GetMapping(value = "/refreshMusic163Fav")
    public void refreshMusic163Fav() {
        DataGraph graph = dataGraphService.refreshCrawler(crawlerMusic163Fav);
    }

    @GetMapping(value = "/refreshJjwxcFav")
    public void refreshJjwxcFav() {
        DataGraph graph = dataGraphService.refreshCrawler(crawlerJjwxcFav);
        // 刷新小说
        Author.graphFlat(graph).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
    }

    @GetMapping(value = "/refreshJjwxcAuthor")
    public void refreshJjwxcAuthor(@RequestParam(required = false) String id) {
        List<DataCard> cards = StringUtils.isNotBlank(id) ? List.of(dataCardService.getById(id))
                : dataCardService.list(Wrappers.lambdaQuery(DataCard.class).eq(DataCard::getCardGroup, THING_PERSON));
        List<DataClue> clues = dataClueService.list(Wrappers.lambdaQuery(DataClue.class)
                .eq(DataClue::getClueGroup, LIB_STORE_JJWXC)
                .eq(StringUtils.isNotBlank(id), DataClue::getSource, id));
        DataGraph graph = new DataGraph(cards, clues);
        // 刷新小说
        Author.graphFlat(graph).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
    }

}
