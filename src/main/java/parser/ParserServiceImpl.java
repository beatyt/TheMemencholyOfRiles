package main.java.parser;

import com.google.inject.Inject;
import main.java.api.ParserService;
import main.java.api.StorageService;
import main.java.app.MySharedQueue;
import main.java.app.PropertyHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.util.Iterator;
import java.util.LinkedHashSet;
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

    public String parseTitle(String headline) {
        String title = headline.replace("Permalink", "");
        if (!"".equals(title)) {
            return title;
        }
        return null;
    }
    public String parseDict(String title, LinkedHashSet names) {
        String fixedTitle = title;
        Iterator<String> nameIter = names.iterator();
        while (nameIter.hasNext()) {
            String name = nameIter.next();
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
        return ""; // null broke the stack
    }

    @Override
    public Void call() throws Exception {
        String dataFile = PropertyHandler.getInstance().getValue("dataFile");
        try {
            Element data = (Element) queue.get();
            while (queue.continueProducing || data != null) {
                logger.info("Processing: " + data);
                data = (Element) queue.get();
                String title = parseTitle(data.text());
                LinkedHashSet<String> checkAgainst = storageService.loadFile(dataFile);
//                LinkedHashSet dedupedList = new LinkedHashSet<>(checkAgainst);
                String parsedTitle = parseDict(title, checkAgainst);
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
