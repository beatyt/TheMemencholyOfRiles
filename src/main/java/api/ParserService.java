package main.java.api;

import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface ParserService {
    public List<String> parseTitles(Elements headlines, List<String> titles);
    public List<String> parseDict(List<String> titles, List<String> names);
}
