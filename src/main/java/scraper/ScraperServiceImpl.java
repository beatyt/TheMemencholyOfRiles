package main.java.scraper;

import main.java.api.ScraperService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by user on 2016-02-10.
 */
public class ScraperServiceImpl implements ScraperService {
    @Override
    public Elements fetchContent(String url, String pattern) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements body = doc.select("body");
        Elements headlines = doc.select("body a[rel=bookmark]");
        return headlines;
    }
}
