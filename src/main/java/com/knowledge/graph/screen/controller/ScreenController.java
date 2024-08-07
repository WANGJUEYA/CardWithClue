package com.knowledge.graph.screen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.common.constant.GraphTypeEnum;
import com.knowledge.graph.common.entity.DataResponse;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;
    @Resource
    private IDataGraphService dataGraphService;

    @GetMapping(value = "/datasets")
    public DataResponse<DataGraph> datasets() {
        return datasetsExcludeLibStore();
    }

    @GetMapping(value = "/datasets/{type}")
    public DataResponse<DataGraph> datasetsWithType(@PathVariable GraphTypeEnum type) {
        return switch (type) {
            case AUTHOR -> DataResponse.ok(dataGraphService.getDataGraphAuthor());
            case ARTIST -> DataResponse.ok(dataGraphService.getDataGraphArtist());
            case ARTIST_SONG -> DataResponse.ok(dataGraphService.getDataGraphArtistSong());
            default -> datasetsExcludeLibStore();
        };
    }

    @GetMapping(value = "/datasetsAll")
    public DataResponse<DataGraph> datasetsAll() {
        DataGraph result = new DataGraph(dataCardService.list(), dataClueService.list());
        return DataResponse.ok(result);
    }

    @GetMapping(value = "/datasetsExcludeLibStore")
    public DataResponse<DataGraph> datasetsExcludeLibStore() {
        List<DataCard> cards = dataCardService.list(Wrappers.lambdaQuery(DataCard.class)
                .notIn(DataCard::getDataGroup, CardGroupEnum.LIB.name()));
        List<DataClue> clues = dataClueService.list(Wrappers.lambdaQuery(DataClue.class)
                .notLike(DataClue::getDataGroup, ClueGroupEnum.LIB_STORE.name()));
        DataGraph result = new DataGraph(cards, clues);
        return DataResponse.ok(result);
    }

}
