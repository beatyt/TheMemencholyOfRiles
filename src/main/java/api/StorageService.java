package api;

import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016-02-10.
 */
public interface StorageService {
    List<String> retrieveEntries() throws IOException;
    void storeEntry(List<String> contentse) throws IOException;
}
