package main.java.app;

import main.java.api.ParserService;
import main.java.api.ScraperService;
import main.java.api.StorageService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

public class Driver {
    private static final Logger logger = LogManager.getLogger(Driver.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new Application());
        ScraperService scraperService = injector.getInstance(ScraperService.class);
        ParserService parserService = injector.getInstance(ParserService.class);
        StorageService storageService = injector.getInstance(StorageService.class);

        logger.info("Starting at: " + LocalDateTime.now());
        Application app = injector.getInstance(Application.class);
        app.theThing();
        logger.info("Finishing at: " + LocalDateTime.now());

//        storageService.testConnection();




    }




}
