package app;

import api.Configuration;
import api.ParserService;
import api.ScraperService;
import api.StorageService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserServiceImpl;
import scraper.ScraperServiceImpl;
import storage.StorageServiceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by user on 2016-02-10.
 */

public class Module extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(Module.class);
    private ScraperService scraperService;
    private ParserService parserService;
    private StorageService storageService;
    private Configuration configuration;

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
    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(ParserService.class).to(ParserServiceImpl.class);
        bind(ScraperService.class).to(ScraperServiceImpl.class);
        bind(StorageService.class).to(StorageServiceImpl.class);
        bind(Configuration.class).to(PropertyHandler.class);
    }

    void start() {
        new Thread(() -> {
            try {
                scraperService.call();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    public java.util.List<String> pickNRandom(java.util.List<String> lst, int n) {
        java.util.List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
