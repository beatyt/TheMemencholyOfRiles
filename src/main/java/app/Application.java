package main.java.app;

import main.java.api.ParserService;
import main.java.api.ScraperService;
import main.java.api.StorageService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import main.java.storage.StorageServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.select.Elements;
import main.java.parser.ParserServiceImpl;
import main.java.scraper.ScraperServiceImpl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.*;

/**
 * Created by user on 2016-02-10.
 */

public class Application extends AbstractModule {
    private static final Logger logger = LogManager.getLogger(Application.class);
    private ScraperService scraperService;
    private ParserService parserService;
    private StorageService storageService;

    @Inject
    public void setScraper(ScraperService scraperService) {
        this.scraperService = scraperService;
    }
    @Inject
    public void setParser(ParserService parserService) {
        this.parserService = parserService;
    }
    @Inject
    public void setScraper(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void configure() {
        bind(ParserService.class).to(ParserServiceImpl.class);
        bind(ScraperService.class).to(ScraperServiceImpl.class);
        bind(StorageService.class).to(StorageServiceImpl.class);
    }

    public void theThing() throws IOException, InterruptedException {
        logger.info("Application is running.");
        String dataFile = PropertyHandler.getInstance().getValue("dataFile");
        String baseUrl = PropertyHandler.getInstance().getValue("baseUrl");
        String saveToFileName = PropertyHandler.getInstance().getValue("saveToFileName");
        java.util.List<String> names = new ArrayList<String>();

        Elements headlines = null;
        int count = 1;
        while (count < 10) {
            // TODO: make main.java.parser and main.java.scraper run in parallel with that dish washer and cleaner design pattern
            String url = baseUrl + count;
            headlines = scraperService.fetchContent(url, "body a[rel=bookmark]");
            java.util.List<String> titles = new ArrayList<String>();
            titles = parserService.parseTitles(headlines, titles);
            names = storageService.loadFile(dataFile);
            titles = parserService.parseDict(titles, names);
            count++;
            storageService.saveFile(titles,saveToFileName);
            Thread.sleep(1000);
        }
        logger.info("Application is done.");
    }

    public void copyToClipboard(String toCopy) {
        StringSelection stringSelection = new StringSelection(toCopy);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    public java.util.List<String> pickNRandom(java.util.List<String> lst, int n) {
        java.util.List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
