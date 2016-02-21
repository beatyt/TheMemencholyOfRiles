package main.java.storage;

import main.java.api.StorageService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public class StorageServiceImpl implements StorageService {
    private static final Logger logger = LogManager.getLogger(StorageServiceImpl.class);


    @Override
    public void storeEntry(String entry) {

    }

    @Override
    public String retrieveEntry() {
        return null;
    }

    @Override
    public void saveFile(List<String> contents, String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("data/" + fileName).getPath());
        logger.info("Writing to file: " + file.getAbsolutePath());
        // TODO:  refactor using streams or something java 8?
        List<String> checkAgainst = loadFile(fileName);
        boolean addTheLine = true;
        for (String s : contents) {
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
//        printList(contents);
    }
    public List<String> loadFile(String fileName) throws IOException {
        List<String> names = new ArrayList<String>();
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

    private List<String> getFileLinesAsList(String fileName){

        List<String> result = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.readLines(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static void printList(List<String> list) {
        for (String t : list) {
            System.out.println(t);
        }
    }
    public void testConnection() {
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("test");
        DBCollection coll = db.getCollection("headlines");
        BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("info", new BasicDBObject("x", 203).append("y", 102));
        coll.insert(doc);
    }
}
