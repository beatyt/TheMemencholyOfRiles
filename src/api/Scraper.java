package api;

import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by user on 2016-02-10.
 */
public interface Scraper {
    public Elements fetchContent(String url, String pattern) throws IOException;
}
