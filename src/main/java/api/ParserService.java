package main.java.api;

import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface ParserService {
    public String parseTitle(String headline);
    public String parseDict(String title, List<String> checkAgainst);
}
