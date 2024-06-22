package com.knowledge.graph.store.controller;

import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163FavImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store/graph")
public class DataGraphController {

    @Resource
    private IDataGraphService dataGraphService;
    @Resource
    private CrawlerMusic163FavImpl crawlerMusic163Fav;

    @GetMapping(value = "/refreshMusic163Fav")
    public void refreshMusic163Fav() {
        dataGraphService.refreshCrawler(crawlerMusic163Fav);
    }


}
