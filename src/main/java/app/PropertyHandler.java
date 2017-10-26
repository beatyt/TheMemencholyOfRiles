package app;

import api.Configuration;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by user on 2016-02-14.
 */
/*
Taken from http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
 */
public class PropertyHandler implements Configuration {

    private static final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);
    private static PropertyHandler instance = null;
    private Properties properties = null;


    @Inject
    private PropertyHandler() {
        properties = new Properties();
        String propFileName = "config.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/" + propFileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static synchronized PropertyHandler getInstance() {
        if (instance == null)
            instance = new PropertyHandler();
        return instance;
    }

    @Override
    public String getValue(String propKey){
        return this.properties.getProperty(propKey);
    }

}