package parser;

import api.Configuration;
import api.StorageService;
import app.MySharedQueue;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(ParserServiceImplTest.class);

    @InjectMocks
    ParserServiceImpl parserService;

    @Mock
    Configuration configuration;

    @Mock
    private MySharedQueue queue;

    @Mock
    private StorageService storageService;

    @Mock
    Element element;

    @Before
    public void setup() throws Exception {
        Mockito.when(configuration.getValue("deleteTags")).thenReturn("Permalink");
        Mockito.when(configuration.getValue("replaceWithName")).thenReturn("Riles");
        Mockito.when(queue.get()).thenReturn(element);
        queue.continueProducing = true;
        Mockito.when(element.text()).thenReturn("123");
        Mockito.when(storageService.retrieveEntries()).thenReturn(Arrays.asList("123", "456"));
        Mockito.doNothing().when(storageService).storeEntry(anyList());
    }

    @Test
    public void parseTitle() throws Exception {
        String headline = "Permalink ASDF";
        String title = parserService.parseTitle(headline);

        assertEquals("It should remove configured tags", title, "ASDF");
    }

    @Test
    public void parseDict() throws Exception {
        Configuration.OPTIONS.REPLACE_NAME_ONCE.setOption(true);
        Configuration.OPTIONS.USE_GENDER_MALE.setOption(true);
        String title = "Her XD 123";
        List<String> names = Arrays.asList("123", "456");
        String result = parserService.parseDict(title, names);

        logger.info(result);
        assertTrue("Her should replace with His", result.contains("His"));
        assertTrue("The name from the configuration should be used", result.contains("Riles"));
    }

    @Test
    public void call() throws Exception {
        parserService.call("ASDF");
    }

}