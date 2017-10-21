package api;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface StorageService {
    void storeEntry(String entry);
    String retrieveEntry();
    List<String> loadFile(File file) throws IOException;
    void saveFile(List<String> contents, String filename) throws IOException;
    void appendLineToFile(String contents, String fileName) throws IOException;
}
