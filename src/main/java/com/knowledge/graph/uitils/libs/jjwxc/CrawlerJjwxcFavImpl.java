package com.knowledge.graph.uitils.libs.jjwxc;

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
import java.util.Comparator;
import java.util.List;

import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.*;

@Slf4j
@Schema(description = "关注作者")
@Component
public class CrawlerJjwxcFavImpl extends AbstractCrawler {

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml(REQUEST_FAV_AUTHOR, HEADER_KEY, HEADER_VALUE);
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
            int books = 0;
            try {
                books = Integer.parseInt(child.children().get(2).text().trim());
            } catch (Exception e) {
                log.error("文章数量获取失败 >>>> {}", e.getMessage());
            }
            request.add(new Author(authorId, authorName, books));
        }
        return request.stream().sorted(Comparator.comparingLong(e -> Long.parseLong(e.getId()))).map(Author::graphItem).toList();
    }

}
