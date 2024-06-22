package com.knowledge.graph.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.graph.common.constant.CardKeyEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.mapper.DataCardMapper;
import com.knowledge.graph.store.service.IDataCardService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author JUE
 */
@Service
public class DataCardServiceImpl extends ServiceImpl<DataCardMapper, DataCard> implements IDataCardService {

    @PostConstruct
    private void init() {
        List<DataCard> item = Arrays.stream(CardKeyEnum.values()).map(CardKeyEnum::card).toList();
        saveOrUpdateBatch(item);
    }

}
