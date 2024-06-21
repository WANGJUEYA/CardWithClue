package com.knowledge.graph.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.mapper.DataCardMapper;
import com.knowledge.graph.store.service.IDataCardService;
import org.springframework.stereotype.Service;

/**
 * @author JUE
 */
@Service
public class DataCardServiceImpl extends ServiceImpl<DataCardMapper, DataCard> implements IDataCardService {

}
