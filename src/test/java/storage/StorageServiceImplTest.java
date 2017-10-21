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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class StorageServiceImplTest {

    private StorageServiceImpl storageService = new StorageServiceImpl();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void storeEntry() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void retrieveEntry() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void saveFile() throws Exception {
        List<String> contents = Arrays.asList("Line 1", "Line 2", "Line 2");
        String fileName = "testFile.txt";
        storageService.saveFile(contents, fileName);

        File file = new File(System.getProperty("user.dir"), fileName);
        
        List<String> results = IOUtils.readLines(new FileInputStream(file));
        assertThat("The file should contain the inserted data", results.containsAll(contents));
        Set<String> resultsAsSet = new LinkedHashSet<>(results);
        assertThat("The results should not contain duplicates", resultsAsSet.size() == results.size());
    }

    @Test
    public void appendLineToFile() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void loadFile() throws Exception {
        fail("Not implemented");
    }

}