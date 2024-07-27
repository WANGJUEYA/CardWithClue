package com.knowledge.graph.store.controller;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcFavImpl;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcNovelImpl;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Author;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163FavImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        DataGraph graph = dataGraphService.refreshCrawler(crawlerJjwxcFav);
        // 刷新小说
        Author.graphFlat(graph.getClues()).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
        CrawlerUtils.clearData();
    }

    @GetMapping(value = "/refreshJjwxcAuthor/{id}")
    public void refreshJjwxcAuthor(@PathVariable String id) {
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        // 刷新小说
        Author.graphFlat(Set.of(id.split(","))).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
        CrawlerUtils.clearData();
    }

}
