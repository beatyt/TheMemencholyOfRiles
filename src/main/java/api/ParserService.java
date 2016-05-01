package main.java.api;

import java.util.LinkedHashSet;

/**
 * Created by user on 2016-02-10.
 */
public interface ParserService {
    public String parseTitle(String headline);
    public String parseDict(String title, LinkedHashSet checkAgainst);
}
