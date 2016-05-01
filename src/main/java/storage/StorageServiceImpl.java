package main.java.storage;

import main.java.api.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @Override
    public void saveFile(LinkedHashSet contents, String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data/" + fileName).getPath());
        logger.info("Writing to file: " + file.getAbsolutePath());
        // TODO:  refactor using streams or something java 8?
        LinkedHashSet<String> checkAgainst = loadFile(fileName);
        boolean addTheLine = true;
        Iterator<String> nameIter = contents.iterator();
        while (nameIter.hasNext()) {
            String s = nameIter.next();
            for (String check : checkAgainst) {
                if (s.equals(check)) {
                    addTheLine = false;
                }
            }
            if (addTheLine) {
                FileUtils.writeStringToFile(file, s + "\r\n", true);
                logger.debug("[Adding] " + s);
            }
        }
    }

    @Override
    public void appendLineToFile(String contents, String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data/" + fileName).getPath());
        logger.info("Writing to file: " + file.getAbsolutePath());
        // TODO:  refactor using streams or something java 8?
        LinkedHashSet<String> checkAgainst = loadFile(fileName);
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

    public LinkedHashSet loadFile(String fileName) throws IOException {
        String path = "data/" + fileName;
//        String fileContents = getFile("data/" + fileName); pretty cool stuff.. gets file contents as a String
        return getFileLinesAsList(path);
    }

    private String getFile(String fileName){

        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private LinkedHashSet getFileLinesAsList(String fileName){

        List<String> result = null;
        LinkedHashSet resultSet = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.readLines(classLoader.getResourceAsStream(fileName));
            resultSet = new LinkedHashSet(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultSet;

    }

}
