package com.knowledge.graph.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public DataGraph refreshCrawler(AbstractCrawler crawler) {
        try {
            Wrapper<DataCard> cardWrapper = crawler.wrapperCard();
            Wrapper<DataClue> clueWrapper = crawler.wrapperClue();
            crawler.setExistGraph(new DataGraph(dataCardService.list(cardWrapper), dataClueService.list(clueWrapper)));
            crawler.execute();
            DataGraph graph = crawler.getExistGraph();
            List<DataCard> updateCard = graph.getCards().stream().filter(i -> Boolean.TRUE.equals(i.getUpdated())).toList();
            if (updateCard.size() > 0) {
                log.debug("处理卡片数据 >>>> {}", updateCard.size());
                dataCardService.saveOrUpdateBatch(updateCard);
                // 使用数据库自增后, id不会自动设置
                graph.setCards(dataCardService.list(cardWrapper));
            }
            List<DataClue> updateClue = graph.getClues().stream().filter(i -> Boolean.TRUE.equals(i.getUpdated())).toList();
            if (updateClue.size() > 0) {
                log.debug("处理线索数据 >>>> {}", updateClue.size());
                for (DataClue i : updateClue) {
                    if (i.getTarget() == null) {
                        i.setTarget(i.getTargetCard().find(graph.getCards()).getId());
                    }
                }
                dataClueService.saveOrUpdateBatch(updateClue);
            }
            graph.setCards(updateCard.stream().map(e -> e.find(graph.getCards())).collect(Collectors.toList()));
            return graph;
        } catch (Exception e) {
            log.error("抓取数据异常", e);
            return new DataGraph(List.of(), List.of());
        }
    }

}
