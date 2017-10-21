package api;

import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface ParserService {
    String parseTitle(String headline);
    String parseDict(String title, List<String> checkAgainst);
}
