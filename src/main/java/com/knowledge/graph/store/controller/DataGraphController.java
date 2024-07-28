package com.knowledge.graph.store.controller;

import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.store.service.IDataCardService;
import com.knowledge.graph.store.service.IDataClueService;
import com.knowledge.graph.store.service.IDataGraphService;
import com.knowledge.graph.uitils.CrawlerUtils;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcFavImpl;
import com.knowledge.graph.uitils.libs.jjwxc.CrawlerJjwxcNovelImpl;
import com.knowledge.graph.uitils.libs.jjwxc.entity.Author;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163AlbumImpl;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163FavImpl;
import com.knowledge.graph.uitils.libs.music163.CrawlerMusic163SongImpl;
import com.knowledge.graph.uitils.libs.music163.entity.Album;
import com.knowledge.graph.uitils.libs.music163.entity.Artist;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/store/graph")
public class DataGraphController {

    @Resource
    private IDataCardService dataCardService;
    @Resource
    private IDataClueService dataClueService;
    @Resource
    private IDataGraphService dataGraphService;
    @Resource
    private CrawlerMusic163FavImpl crawlerMusic163Fav;
    @Resource
    private CrawlerJjwxcFavImpl crawlerJjwxcFav;

    @GetMapping(value = "/refreshMusic163Fav")
    public void refreshMusic163Fav() {
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        DataGraph graph = dataGraphService.refreshCrawler(crawlerMusic163Fav);
        crawlerMusic163(Artist.graphFlat(graph.getClues()));
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
    }

    @GetMapping(value = "/refreshMusic163FavArtist/{id}")
    public void refreshMusic163FavArtist(@PathVariable String id) {
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        // 刷新某个网易歌手的专辑及歌曲; id为网易云歌手的id
        crawlerMusic163(Artist.graphFlat(Set.of(id.split(","))));
        CrawlerUtils.clearData();
    }

    private void crawlerMusic163(List<Artist> artists) {
        for (Artist artist : artists) {
            CrawlerMusic163AlbumImpl albumCrawler = new CrawlerMusic163AlbumImpl(artist);
            dataGraphService.refreshCrawler(albumCrawler);
            List<Album> albums = albumCrawler.getAlbums();
            for (Album album : albums) {
                dataGraphService.refreshCrawler(new CrawlerMusic163SongImpl(artist, album));
            }
        }
    }

    @GetMapping(value = "/refreshJjwxcFav")
    public void refreshJjwxcFav() {
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        DataGraph graph = dataGraphService.refreshCrawler(crawlerJjwxcFav);
        // 刷新小说
        Author.graphFlat(graph.getClues()).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
        CrawlerUtils.clearData();
    }

    @GetMapping(value = "/refreshJjwxcAuthor/{id}")
    public void refreshJjwxcAuthor(@PathVariable String id) {
        CrawlerUtils.loadData(dataCardService.list(), dataClueService.list());
        // 刷新某个晋江作者文章; id为晋江作者的id
        Author.graphFlat(Set.of(id.split(","))).forEach(a -> dataGraphService.refreshCrawler(new CrawlerJjwxcNovelImpl(a)));
        CrawlerUtils.clearData();
    }

}
