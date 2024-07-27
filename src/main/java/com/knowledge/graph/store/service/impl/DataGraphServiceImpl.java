package com.knowledge.graph.store.service.impl;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
            DataGraph graph = crawler.execute();

            List<DataCard> updateCard = graph.getCards().stream().filter(i -> Boolean.TRUE.equals(i.getUpdated())).toList();
            if (!updateCard.isEmpty()) {
                log.debug("处理卡片数据 >>>> {}", updateCard.size());
                // 单条数据新增; 处理使用数据库自增后, id不会自动设置的问题
                List<DataCard> update = updateCard.stream().map(data -> {
                    if (StringUtils.isBlank(data.getId())) {
                        dataCardService.save(data);
                        CrawlerUtils.putDataCardId(data.getId(), data);
                        return null;
                    } else {
                        return data;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                if (!update.isEmpty()) {
                    log.debug("更新卡片数据 >>>> {}", updateCard.size());
                    dataCardService.updateBatchById(update);
                }
            }
            Collection<DataClue> updateClue = graph.getClues().stream().collect(Collectors.toMap(DataClue::getId, Function.identity(), CrawlerUtils::mergeDataClue)).values();
            if (!updateClue.isEmpty()) {
                log.debug("处理线索数据 >>>> {}", updateClue.size());
                dataClueService.saveOrUpdateBatch(updateClue);
            }
            return graph;
        } catch (Exception e) {
            log.error("抓取数据异常", e);
            return new DataGraph(List.of(), List.of());
        }
    }

}
