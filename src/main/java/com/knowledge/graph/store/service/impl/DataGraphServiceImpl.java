package com.knowledge.graph.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.knowledge.graph.common.entity.DataGraph;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JUE
 */
@Slf4j
@Service
public class DataGraphServiceImpl implements IDataGraphService {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;

    @Override
    public void refreshCrawler(AbstractCrawler crawler) {
        Wrapper<DataCard> cardWrapper = crawler.wrapperCard();
        Wrapper<DataClue> clueWrapper = crawler.wrapperClue();
        crawler.setExistGraph(new DataGraph(dataCardService.list(cardWrapper), dataClueService.list(clueWrapper)));
        crawler.execute();
        DataGraph graph = crawler.getExistGraph();
        if (crawler.clearCard()) {
            dataCardService.remove(cardWrapper);
        }
        if (crawler.clearClue()) {
            dataClueService.remove(clueWrapper);
        }
        List<DataCard> updateCard = graph.getNodes().stream().filter(i -> Boolean.TRUE.equals(i.getUpdated())).toList();
        if (updateCard.size() > 0) {
            log.debug("处理卡片数据 >>>> {}", updateCard.size());
            dataCardService.saveOrUpdateBatch(updateCard);
        }
        List<DataClue> updateClue = graph.getLinks().stream().filter(i -> Boolean.TRUE.equals(i.getUpdated())).toList();
        if (updateClue.size() > 0) {
            log.debug("处理线索数据 >>>> {}", updateClue.size());
            for (DataClue i : updateClue) {
                if (i.getTarget() == null) {
                    i.setTarget(i.getTargetCard().find(graph.getNodes()).getId());
                }
            }
            dataClueService.saveOrUpdateBatch(updateClue);
        }
    }

}
