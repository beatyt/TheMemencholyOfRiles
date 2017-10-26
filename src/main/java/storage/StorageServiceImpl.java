package storage;

import api.Configuration;
import api.StorageService;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public class StorageServiceImpl implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    private Configuration configuration;

    @Inject
    public void setConfiguration(Configuration configuration) { this.configuration = configuration; }

    /**
     * Takes a string and appends it to a file
     * Duplicate lines are not appended
     *
     * @param contents Lines to write to a file
     * @throws IOException
     */
    @Override
    public void storeEntry(List<String> contents) throws IOException {
        File file = new File(System.getProperty("user.dir"), configuration.getValue("saveToFileName"));
        logger.info("Writing to file: " + file.getAbsolutePath());
        List<String> checkAgainst = retrieveEntries();

        contents.stream()
                .filter(line -> checkAgainst.stream()
                        .noneMatch(check -> check.equals(line)))
                .forEach(line -> {
                    try {
                        FileUtils.writeStringToFile(file,
                                line + System.getProperty("line.separator"),
                                true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public List<String> retrieveEntries() {
        File file = new File(System.getProperty("user.dir"), configuration.getValue("saveToFileName"));
        try {
            file.createNewFile(); // does nothing if the file already exists
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> result = null;
        try {
            result = IOUtils.readLines(new FileInputStream(file));
            result = new ArrayList<>(new LinkedHashSet<>(result));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
