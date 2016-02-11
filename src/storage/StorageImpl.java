package storage;

import api.Scraper;
import api.Storage;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 2016-02-10.
 */
public class StorageImpl implements Storage {

    @Override
    public void storeEntry(String entry) {

    }

    @Override
    public String retrieveEntry() {
        return null;
    }

    @Override
    public void saveFile(List<String> contents, String filename) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/data/", filename);
        // TODO:  Check for duplicates and do not add anything duplicates
        // TODO:  refactor using streams or something java 8?
        List<String> checkAgainst = loadFile(file);
        boolean addTheLine = true;
        for (String s : contents) {
            for (String check : checkAgainst) {
                if (s.equals(check)) {
                    addTheLine = false;
                }
            }
            if (addTheLine) {
                FileUtils.writeStringToFile(file, s + "\r\n", true);
            }
        }
        printList(contents);
    }
    public List<String> loadFile(File file) throws IOException {
        List<String> names = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line);
            }
        }
        return names;
    }

    public static void printList(List<String> list) {
        for (String t : list) {
            System.out.println(t);
        }
    }
}
