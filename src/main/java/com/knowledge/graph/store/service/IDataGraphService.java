package com.knowledge.graph.store.service;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.libs.AbstractCrawler;

/**
 * @author JUE
 */
public interface IDataGraphService {

    DataGraph refreshCrawler(AbstractCrawler crawler);

}
