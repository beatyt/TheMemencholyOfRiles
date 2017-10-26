package parser;

import api.Configuration;
import api.ParserService;
import api.StorageService;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public class ParserServiceImpl implements ParserService {
    private static final Logger logger = LoggerFactory.getLogger(ParserServiceImpl.class);

    final private StorageService storageService;
    final private Configuration configuration;

    @Inject
    public ParserServiceImpl(StorageService storageService, Configuration configuration) {
        this.storageService = storageService;
        this.configuration = configuration;
    }

    String parseTitle(String headline) {
        List<String> deletions = Arrays.asList(configuration.getValue("deleteTags").split(","));
        for (String deletion : deletions) {
            headline = headline.replace(deletion, "");
        }
        if (!"".equals(headline)) {
            return headline.trim();
        }
        return null;
    }

    String parseDict(String title, List<String> names) {
        String fixedTitle = title;
        for (String name : names) {
            if (Configuration.OPTIONS.REPLACE_NAME_ONCE.isEnabled()) {
                // Quit once we find one match
                if (fixedTitle.contains(name)) {
                    fixedTitle = fixedTitle.replaceFirst(name, configuration.getValue("replaceWithName"));
                    break;
                }
            }
        }
        if (Configuration.OPTIONS.USE_GENDER_MALE.isEnabled()) {
            fixedTitle = fixedTitle.replace("Her", "His");
            fixedTitle = fixedTitle.replace("She", "He");
        }
        if (Configuration.OPTIONS.REPLACE_NAME_ONCE.isEnabled()) {
            int count = StringUtils.countMatches(fixedTitle, "Riles");
            if (count == 1) {
                return fixedTitle;
            }
        }
        return fixedTitle;
    }

    @Override
    public void call(String headline) throws IOException {
        String title = parseTitle(headline);
        logger.info("Processing: " + title);
        List<String> checkAgainst = storageService.retrieveEntries();
        String parsedTitle = parseDict(title, checkAgainst);
        logger.info("Finished processing: " + parsedTitle);
        storageService.storeEntry(Collections.singletonList(parsedTitle));
        logger.info("Consumer is done.");

    }
}
