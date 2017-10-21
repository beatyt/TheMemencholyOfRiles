package storage;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StorageServiceImplTest {

    private StorageServiceImpl storageService = new StorageServiceImpl();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void storeEntry() throws Exception {
    }

    @Test
    public void retrieveEntry() throws Exception {
    }

    @Test
    public void saveFile() throws Exception {
        List<String> contents = Arrays.asList("Line 1", "Line 2");
        String fileName = "testFile.txt";
        storageService.saveFile(contents, fileName);

        File file = new File(System.getProperty("user.dir"), fileName);
        List<String> results = IOUtils.readLines(new FileInputStream(file));

        assert results.containsAll(contents);
        Set<String> resultsAsSet = new LinkedHashSet<>(results);
        assert resultsAsSet.equals(contents);
    }

    @Test
    public void appendLineToFile() throws Exception {
    }

    @Test
    public void loadFile() throws Exception {
    }

}