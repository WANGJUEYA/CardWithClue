package com.knowledge.graph.screen.controller;

import com.knowledge.graph.common.entity.DataCard;
import com.knowledge.graph.common.entity.DataClue;
import com.knowledge.graph.common.entity.DataGraph;
import com.knowledge.graph.common.entity.DataResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/screen")
public class ScreenController {

    @GetMapping(value = "/datasets")
    public DataResponse<DataGraph> datasets() {
        DataGraph result = new DataGraph();
        result.setNodes(new ArrayList<>());
        result.setLinks(new ArrayList<>());

        long end = 10L;
        // 随机生成
        LongStream.range(0, end)
                .forEach(i -> {
                    String id = String.valueOf(i);
                    DataCard card = new DataCard();
                    card.setId(id);
                    result.getNodes().add(card);

                    DataClue clue = new DataClue();
                    clue.setSource(id);
                    clue.setTarget(String.valueOf(new Random().nextLong(end)));
                    result.getLinks().add(clue);
                });


        return DataResponse.ok(result);
    }

}
