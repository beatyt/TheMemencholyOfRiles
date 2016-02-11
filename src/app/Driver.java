package app;

import api.Parser;
import api.Scraper;
import api.Storage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new Application());

        Scraper scraperService = injector.getInstance(Scraper.class);
        Parser parserService = injector.getInstance(Parser.class);
        Storage storageService = injector.getInstance(Storage.class);

        File celebNames = new File("data/celeb-names");
        File countryNames = new File("data/country-names");
        List<String> names = new ArrayList<String>();
        String baseUrl = "http://perezhilton.com/page/";

        Elements headlines = null;
        int count = 1;
        while (count < 10) {
            // TODO: make parser and scraper run in parallel with that design dish washer and cleaner design pattern
            String url = baseUrl + count;
            headlines = scraperService.fetchContent(url, "body a[rel=bookmark]");
            List<String> titles = new ArrayList<String>();
            titles = parserService.parseTitles(headlines, titles);
            names = storageService.loadFile(celebNames);
            titles = parserService.parseDict(titles, names);
            count++;
            storageService.saveFile(titles,"Riles.txt");
            Thread.sleep(1000);
        }

    }




    public static void copyToClipboard(String toCopy) {
        StringSelection stringSelection = new StringSelection(toCopy);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
