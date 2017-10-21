package parser;

import api.ParserService;
import api.StorageService;
import app.MySharedQueue;
import app.PropertyHandler;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by user on 2016-02-10.
 */
public class ParserServiceImpl implements ParserService, Callable<Void> {
    private static final Logger logger = LogManager.getLogger(ParserServiceImpl.class);
    private StorageService storageService;

    @Inject
    public void setStorage(StorageService storageService) {
        this.storageService = storageService;
    }

    @Inject
    private MySharedQueue queue;

    @Override
    public String parseTitle(String headline) {
        String title = headline.replace("Permalink", "");
        if (!"".equals(title)) {
            return title;
        }
        return null;
    }

    @Override
    public String parseDict(String title, List<String> names) {
        String fixedTitle = title;
            for (String name : names) {
                // Quit once we find one match
                if (fixedTitle.contains(name)) {
                    fixedTitle = fixedTitle.replaceFirst(name, "Riles");
                    break;
                }
        }
        fixedTitle = fixedTitle.replace("Her", "His");
        fixedTitle = fixedTitle.replace("She", "He");
        int count = StringUtils.countMatches(fixedTitle, "Riles");
        if (count == 1) {
            // TODO:  Does this need to go outside the loop?  Or does it need to work on batch for the queue?
            return fixedTitle;
        }
        return null;
    }

    @Override
    public Void call() throws IOException {
        String dataFile = PropertyHandler.getInstance().getValue("dataFile");
        try {
            Element data = (Element) queue.get();
            while (queue.continueProducing || data != null) {
                data = (Element) queue.get();
                String title = parseTitle(data.text());
                List<String> checkAgainst = storageService.loadFile(dataFile);
                List<String> dedupedList =
                        new ArrayList<>(new LinkedHashSet<>(checkAgainst));
                String parsedTitle = parseDict(title, dedupedList);
                logger.info("Finished processing: " + parsedTitle);
//                storageService.storeEntry(parsedTitle);
                storageService.appendLineToFile(parsedTitle, "Riles.txt");
//                System.out.println("Consumer processed: " + parsedTitle);
                Thread.sleep(100);
            }
            System.out.println("Consumer is done.");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
