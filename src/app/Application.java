package app;

import api.Parser;
import api.Scraper;
import api.Storage;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import parser.ParserImpl;
import scraper.ScraperImpl;
import storage.StorageImpl;

/**
 * Created by user on 2016-02-10.
 */
public class Application extends AbstractModule {

    private Scraper scraper;
    private Parser parser;
    private Storage storage;

    @Inject
    public void setScraper(Scraper scraper) {
        this.scraper = scraper;
    }
    @Inject
    public void setParser(Parser parser) {
        this.parser = parser;
    }
    @Inject
    public void setScraper(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected void configure() {
        bind(Parser.class).to(ParserImpl.class);
        bind(Scraper.class).to(ScraperImpl.class);
        bind(Storage.class).to(StorageImpl.class);
    }
}
