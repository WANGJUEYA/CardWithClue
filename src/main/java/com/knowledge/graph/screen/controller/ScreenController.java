package com.knowledge.graph.screen.controller;

import com.knowledge.graph.common.entity.DataGraph;
import com.knowledge.graph.common.entity.DataResponse;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;

    @GetMapping(value = "/datasets")
    public DataResponse<DataGraph> datasets() {
        DataGraph result = new DataGraph();
        result.setNodes(dataCardService.list());
        result.setLinks(dataClueService.list());
        return DataResponse.ok(result);
    }

    @GetMapping(value = "/random")
    public DataResponse<DataGraph> random() {
        DataGraph result = new DataGraph();
        result.setNodes(new ArrayList<>());
        result.setLinks(new ArrayList<>());

        long end = 10L;
        // 随机生成
        LongStream.range(0, end)
                .forEach(i -> {
                    DataCard card = new DataCard();
                    card.setId(i);
                    result.getNodes().add(card);

                    DataClue clue = new DataClue();
                    clue.setSource(i);
                    clue.setTarget(new Random().nextLong(end));
                    result.getLinks().add(clue);
                });


        return DataResponse.ok(result);
    }

}
