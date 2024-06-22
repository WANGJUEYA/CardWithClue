package com.knowledge.graph.screen.controller;

import com.knowledge.graph.common.entity.DataResponse;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;

    @GetMapping(value = "/datasets")
    public DataResponse<DataGraph> datasets() {
        DataGraph result = new DataGraph(dataCardService.list(), dataClueService.list());
        return DataResponse.ok(result);
    }

}
