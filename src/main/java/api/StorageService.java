package main.java.api;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * Created by user on 2016-02-10.
 */
public interface StorageService {
    public void storeEntry(String entry);
    public String retrieveEntry();
    public LinkedHashSet loadFile(String path) throws IOException;
    public void saveFile(LinkedHashSet contents, String filename) throws IOException;
    public void appendLineToFile(String contents, String fileName) throws IOException;
}
