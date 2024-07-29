package com.knowledge.graph.uitils.libs.jjwxc;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.AbstractCrawler;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Author;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Novel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.*;

@Slf4j
@Schema(description = "作者专栏")
public class CrawlerJjwxcNovelImpl extends AbstractCrawler {

    private final Author author;

    @Getter
    private final List<Novel> request = new ArrayList<>();

    public CrawlerJjwxcNovelImpl(Author author) {
        this.author = author;
    }

    @Override
    public List<DataGraph> crawler() {
        Document html = CrawlerUtils.getHtml(REQUEST_AUTHOR_PRE + author.getId(), HEADER_KEY, HEADER_VALUE);
        Element doc;
        if (html == null || (doc = html.getElementsByTag("table").get(7)) == null) {
            return new ArrayList<>();
        }

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
                // 列出小说名称
                String novelName = title.get(0).text().trim();
                if (novelName.endsWith(" [锁]")) {
                    continue;
                }
                String novelId = novelHref.split("novelid=")[1];

                // 同名的小说存在可能性: 番外集, 带上作者名字
                request.add(new Novel(novelId, author.getName(), novelName));
            } catch (Exception e) {
                log.error("数据获取失败 >>> title:{}, novelHref:{}", title.text(), novelHref);
            }
        }
        return request.stream().map(a -> a.graphItem(author.createCard())).toList();
    }

}
