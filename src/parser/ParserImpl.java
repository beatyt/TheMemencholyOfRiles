package parser;

import api.Parser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public class ParserImpl implements Parser {
    public List<String> parseTitles(Elements headlines, List<String> titles) {
        for (Element headline : headlines) {
            String s = headline.text().replace("Permalink", "");
            if (!"".equals(s)) {
                titles.add(s);
            }
        }
        return titles;
    }
    public List<String> parseDict(List<String> titles, List<String> names) {
        List<String> fixedTitles = new ArrayList<String>();
        for (String title : titles) {
            for (String name : names) {
                // TODO:  Change this so it will only replace a first occurrence
                /*
                Could possibly use a KV structure, get true on a match, and then replace only that?
                 */
                title = title.replace(name, "Riles");
            }
            title = title.replace("Her", "His");
            title = title.replace("She", "He");
            int count = StringUtils.countMatches(title, "Riles");
            if (count == 1) {
                fixedTitles.add(title);
            }
        }
        return fixedTitles;
    }
}
