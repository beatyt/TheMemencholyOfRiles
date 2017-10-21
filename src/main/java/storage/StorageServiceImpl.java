package storage;

import api.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(StorageServiceImpl.class);


    @Override
    public void storeEntry(String entry) {
        System.out.println(entry);
    }

    @Override
    public String retrieveEntry() {
        return null;
    }

    /**
     * Takes a string and appends it to a file
     * Duplicate lines are not appended
     *
     * @param contents Lines to write to a file
     * @param fileName Named file which will be created where program runs
     * @throws IOException
     */
    @Override
    public void saveFile(List<String> contents, String fileName) throws IOException {
        File file = new File(System.getProperty("user.dir"), fileName);
        logger.info("Writing to file: " + file.getAbsolutePath());
        List<String> checkAgainst = loadFile(file);

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

    @Override
    public void appendLineToFile(String contents, String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data/" + fileName).getPath());
        logger.info("Writing to file: " + file.getAbsolutePath());
        // TODO:  refactor using streams or something java 8?
        List<String> checkAgainst = loadFile(file);
        boolean addTheLine = true;
        for (String check : checkAgainst) {
            if (contents.equals(check)) {
                addTheLine = false;
            }
        }
        if (addTheLine &&
                "" != contents &&
                contents.length() > 10) {
            FileUtils.writeStringToFile(file, contents + "\r\n", true);
            logger.debug("Writing line:   " + contents);
        }
    }

    public List<String> loadFile(File file) {
        try {
            file.createNewFile();
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
