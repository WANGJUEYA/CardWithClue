package com.knowledge.graph.uitils.libs.jjwxc;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.CardKeyEnum;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Author;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Novel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.HEADER_KEY;
import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.HEADER_VALUE;

@Slf4j
@Schema(description = "作者专栏")
public class CrawlerJjwxcNovelImpl extends AbstractCrawler {

    private final Author author;

    public CrawlerJjwxcNovelImpl(Author author) {
        this.author = author;
    }

    @Override
    public Wrapper<DataCard> wrapperCard() {
        return Wrappers.lambdaQuery(DataCard.class)
                .in(DataCard::getCardGroup, CardGroupEnum.THING_BOOK, CardGroupEnum.THING_PERSON);
    }

    @Override
    public Wrapper<DataClue> wrapperClue() {
        return Wrappers.lambdaQuery(DataClue.class)
                .in(DataClue::getClueGroup, ClueGroupEnum.LIB_STORE_JJWXC, ClueGroupEnum.WRITER);
    }

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml("https://www.jjwxc.net/oneauthor.php?authorid=" + author.getId(), HEADER_KEY, HEADER_VALUE);
        Element doc;
        if (html == null || (doc = html.getElementsByTag("table").get(7)) == null) {
            return new ArrayList<>();
        }

        List<Novel> request = new ArrayList<>();
        for (Element element : doc.getElementsByTag("tr")) {
            Elements title = element.getElementsByTag("a");
            if (title.isEmpty()) {
                continue;
            }
            String novelHref = title.get(0).attributes().get("href");
            if (novelHref == null || novelHref.length() == 0) {
                continue;
            }
            try {
                // 列出作者名称
                String novelName = author.getName() + " " + title.get(0).text().trim();
                if (novelName.endsWith(" [锁]")) {
                    continue;
                }
                String novelId = novelHref.split("novelid=")[1];
                request.add(new Novel(novelId, novelName));
            } catch (Exception e) {
                log.error("数据获取失败 >>> title:{}, novelHref:{}", title.text(), novelHref);
            }
        }
        return request.stream().map(a -> a.graphItem(CardKeyEnum.JJWXC.card(), author.getAuthor())).toList();
    }

}
