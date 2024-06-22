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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.HEADER_KEY;
import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.HEADER_VALUE;

@Slf4j
@Schema(description = "关注作者")
@Component
public class CrawlerJjwxcFavImpl extends AbstractCrawler {

    @Override
    public Wrapper<DataCard> wrapperCard() {
        return Wrappers.lambdaQuery(DataCard.class)
                .eq(DataCard::getCardGroup, CardGroupEnum.THING_PERSON);
    }

    @Override
    public Wrapper<DataClue> wrapperClue() {
        return Wrappers.lambdaQuery(DataClue.class)
                .eq(DataClue::getClueGroup, ClueGroupEnum.LIB_STORE_JJWXC);
    }

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml("https://my.jjwxc.net/backend/favoriteauthor.php", HEADER_KEY, HEADER_VALUE);
        Element doc;
        if (html == null || (doc = html.getElementById("fav_author_table")) == null) {
            return new ArrayList<>();
        }

        List<Author> request = new ArrayList<>();
        for (Element child : doc.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            Element item = child.children().get(1);
            String authorName = item.text().trim();
            String authorHref = item.child(0).attributes().get("href");
            String authorId = authorHref.split("authorid=")[1];
            request.add(new Author(authorId, authorName));
        }
        return request.stream().map(a -> a.graphItem(CardKeyEnum.JJWXC.card())).toList();
    }

}
