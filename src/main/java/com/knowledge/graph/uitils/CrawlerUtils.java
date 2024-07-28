package com.knowledge.graph.uitils;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CrawlerUtils {

    private CrawlerUtils() {
    }

    public static final Map<String, DataCard> CARD_MAP_ID = new HashMap<>();
    public static final Map<String, DataCard> CARD_MAP_KEY = new HashMap<>();
    public static final Map<String, DataClue> CLUE_MAP_ID = new HashMap<>();

    public static void loadData(List<DataCard> dataCardList, List<DataClue> dataClueList) {
        for (DataCard dataCard : dataCardList) {
            CARD_MAP_ID.put(dataCard.getId(), dataCard);
            CARD_MAP_KEY.put(dataCard.getMapKey(), dataCard);
        }
        for (DataClue dataClue : dataClueList) {
            CLUE_MAP_ID.put(dataClue.getId(), dataClue);
        }
        log.info("数据加载完成 >>>>>>>>>>>>>>>>>>>>>>>> 同步远端仓库源开始");
    }

    public static void clearData() {
        CARD_MAP_ID.clear();
        CARD_MAP_KEY.clear();
        CLUE_MAP_ID.clear();
        log.info("数据清理完成 >>>>>>>>>>>>>>>>>>>>>>>> 同步远端仓库源结束");
    }

    public static DataCard getDataCardId(String id) {
        return CARD_MAP_ID.get(id);
    }

    public static void putDataCardId(String id, DataCard dataCard) {
        CARD_MAP_ID.put(id, dataCard);
    }

    public static DataCard getDataCard(String mapKey) {
        if (CARD_MAP_KEY.containsKey(mapKey)) {
            return CARD_MAP_KEY.get(mapKey);
        } else {
            String[] key = mapKey.split("@-@");
            CardGroupEnum group = CardGroupEnum.valueOf(key[0]);
            if (key.length == 2) {
                return getDataCard(group, key[1]);
            } else if (key.length == 3) {
                return getDataCard(group, key[1], key[2]);
            }
            throw new RuntimeException("mayKey不符合要求 >>> " + mapKey);
        }
    }

    public static DataCard getDataCard(CardGroupEnum group, String key) {
        return getDataCard(group, key, null);
    }

    public static DataCard getDataCard(CardGroupEnum group, String key, String annotation) {
        DataCard dataCard = new DataCard(group, key, annotation);
        String mapKey = dataCard.getMapKey();
        if (CARD_MAP_KEY.containsKey(mapKey)) {
            return CARD_MAP_KEY.get(mapKey);
        } else {
            dataCard.setUpdated(true);
            CARD_MAP_KEY.put(mapKey, dataCard);
            return dataCard;
        }
    }

    public static DataClue getDataClue(String id) {
        if (CLUE_MAP_ID.containsKey(id)) {
            return CLUE_MAP_ID.get(id);
        } else {
            String[] key = id.split("-");
            ClueGroupEnum group = ClueGroupEnum.valueOf(key[0]);
            return getDataClue(group, key[1], key[2]);
        }
    }

    public static DataClue getDataClue(ClueGroupEnum group, String source, String target) {
        DataClue dataClue = new DataClue(group, source, target);
        String id = dataClue.getId();
        if (CLUE_MAP_ID.containsKey(id)) {
            return CLUE_MAP_ID.get(id);
        } else {
            dataClue.setUpdated(true);
            CLUE_MAP_ID.put(id, dataClue);
            return dataClue;
        }
    }


    public static DataCard mergeDataCard(DataCard newData) {
        DataCard exist = getDataCard(newData.getMapKey());
        exist.mergeFrom(newData);
        return exist;
    }


    public static DataClue mergeDataClue(DataClue newData, DataClue... newDataList) {
        DataClue exist = getDataClue(newData.getId());
        exist.mergeFrom(newData);
        for (DataClue item : newDataList) {
            exist.mergeFrom(item);
        }
        return exist;
    }


    public static Document getHtml(String url, String headerKey, String headerValue) {
        try {
            log.info("开始抓取数据 >>>> {}", url);
            return Jsoup.connect(url).header(headerKey, headerValue).execute().parse();
        } catch (Exception e) {
            log.error("抓取数据失败 >>>> ", e);
            return null;
        }
    }

    public static String getJsonStr(String url, String requestMethod, String headerKey, String headerValue) {
        try {
            CrawlerSslUtils.ignoreSsl();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            // 设置请求方法
            connection.setRequestMethod(StringUtils.defaultIfBlank(requestMethod, "GET"));
            /// 请求头
            connection.setRequestProperty(headerKey, headerValue);
            // 发送请求
            connection.connect();
            // 检查响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jsonStr = CharStreams.fromStream(connection.getInputStream()).toString();
                log.debug("{} >>> \n{}", url, jsonStr);
                return jsonStr;
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
