package scraper;

import com.google.inject.Inject;
import api.ScraperService;
import app.MySharedQueue;
import app.PropertyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Spliterator;
import java.util.concurrent.Callable;

/**
 * Created by user on 2016-02-10.
 */
public class ScraperServiceImpl implements ScraperService, Callable<Void> {
    private static final Logger logger = LogManager.getLogger(ScraperServiceImpl.class);

    @Inject
    private MySharedQueue queue;

    @Override
    public Elements fetchContent(String url, String pattern) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements body = doc.select("body");
        Elements headlines = doc.select("body a[rel=bookmark]");
        return headlines;
    }

    @Override
    public Void call() throws Exception {

        String baseUrl = PropertyHandler.getInstance().getValue("baseUrl");
        String urlPageSuffix = PropertyHandler.getInstance().getValue("urlPageSuffix");
        String numberOfPages = PropertyHandler.getInstance().getValue("numberOfPages");
        String tagToParse = PropertyHandler.getInstance().getValue("tagToParse");
        String debug = PropertyHandler.getInstance().getValue("debug");

        assert(numberOfPages.matches("^-?\\d+$")); // make sure someone supplied only numbers

        Elements headlines = null;
        int goal = Integer.parseInt(numberOfPages);
        try {
            for (int i = 0; i < goal; i++) {
                String url = baseUrl + urlPageSuffix + i;
                if (debug.equals("true")) {

                }
                else {
                    headlines = fetchContent(url, tagToParse);
                }
                Spliterator<Element> elementSpliterator = headlines.spliterator().trySplit();
                elementSpliterator.forEachRemaining(headline -> {
                    try {
                        if (!headline.text().toLowerCase().contains("permalink")
                                && ""   != headline.text()
                                && null != headline.text()) {
                            logger.info("Produced: " + headline);
                            queue.put(headline);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(1000);
            }
            this.queue.continueProducing = Boolean.FALSE;
            System.out.println("Producer finished its job; terminating.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
