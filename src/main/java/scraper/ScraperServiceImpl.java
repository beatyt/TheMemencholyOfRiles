package scraper;

import api.ParserService;
import api.ScraperService;
import app.PropertyHandler;
import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Spliterator;

/**
 * Created by user on 2016-02-10.
 */
public class ScraperServiceImpl implements ScraperService, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ScraperServiceImpl.class);

    private final ParserService parserService;

    @Inject
    public ScraperServiceImpl(ParserService parserService) {
        this.parserService = parserService;
    }

    private Elements fetchContent(String url, String pattern) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements body = doc.select("body");
        return doc.select("body a[rel=bookmark]");
    }

    @Override
    public void call() throws IOException, InterruptedException {
        String baseUrl = PropertyHandler.getInstance().getValue("baseUrl");
        String urlPageSuffix = PropertyHandler.getInstance().getValue("urlPageSuffix");
        String targetNumberOfPages = PropertyHandler.getInstance().getValue("numberOfPages");
        String tagToParse = PropertyHandler.getInstance().getValue("tagToParse");

        assert (targetNumberOfPages.matches("^-?\\d+$")); // make sure someone supplied only numbers

        final int goal = Integer.parseInt(targetNumberOfPages);
        for (int i = 0; i < goal; i++) {
            final String url = baseUrl + urlPageSuffix + i;
            final Elements headlines = fetchContent(url, tagToParse);

            final Spliterator<Element> elementSplitter = headlines.spliterator().trySplit();
            elementSplitter.forEachRemaining(headline -> {
                if (!headline.text().toLowerCase().contains("permalink")
                        && !"".equals(headline.text())
                        && null != headline.text()) {
                    logger.info("Produced: " + headline);
                    try {
                        parserService.call(headline.text());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(1000);
        }
        logger.info("Producer finished its job; terminating.");
    }

    @Override
    public void run() {
        try {
            call();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
