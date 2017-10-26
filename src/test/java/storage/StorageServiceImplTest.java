package storage;

import api.Configuration;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImplTest.class);

    @InjectMocks
    private StorageServiceImpl storageService;

    @Mock
    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        Mockito.when(configuration.getValue("saveToFileName")).thenReturn("testFile.txt");
    }

    @Test
    public void saveFile() throws Exception {
        List<String> contents = Arrays.asList("Line 1", "Line 2", "Line 2");
        String fileName = "testFile.txt";
        storageService.storeEntry(contents);

        File file = new File(System.getProperty("user.dir"), fileName);

        List<String> results = IOUtils.readLines(new FileInputStream(file));
        assertThat("The file should contain the inserted data", results.containsAll(contents));
        Set<String> resultsAsSet = new LinkedHashSet<>(results);
        assertThat("The results should not contain duplicates", resultsAsSet.size() == results.size());
    }

    @Test
    public void loadFile() throws Exception {
        List<String> contents = storageService.retrieveEntries();

        assertNotNull(contents);
    }

}