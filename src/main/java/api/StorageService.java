package main.java.api;

import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface StorageService {
    public void storeEntry(String entry);
    public String retrieveEntry();
    public List<String> loadFile(String path) throws IOException;
    public void saveFile(List<String> contents, String filename) throws IOException;
}
