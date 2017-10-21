package app;

import api.ParserService;
import api.ScraperService;
import api.StorageService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import storage.StorageServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.select.Elements;
import parser.ParserServiceImpl;
import scraper.ScraperServiceImpl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by user on 2016-02-10.
 */

public class Module extends AbstractModule {
    private static final Logger logger = LogManager.getLogger(Module.class);
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
    public void setStorage(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void configure() {
        bind(ParserService.class).to(ParserServiceImpl.class);
        bind(ScraperService.class).to(ScraperServiceImpl.class);
        bind(StorageService.class).to(StorageServiceImpl.class);
    }

    public void start() throws IOException, InterruptedException {
        String saveToFileName = PropertyHandler.getInstance().getValue("saveToFileName");
//        BlockingQueue sharedQueue = new LinkedBlockingQueue();
        final int NTHREADS = 25;
        final ExecutorService exec =
                Executors.newFixedThreadPool(NTHREADS);

        /* These statements will start the app */
        exec.submit((Callable<Object>) parserService);
        exec.submit((Callable<Object>) scraperService);
    }



    public java.util.List<String> pickNRandom(java.util.List<String> lst, int n) {
        java.util.List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
