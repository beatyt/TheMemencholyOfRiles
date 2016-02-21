package main.java.app;

import main.java.api.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by user on 2016-02-14.
 */
/*
Taken from http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
 */
public class PropertyHandler implements Configuration {

    private static final Logger logger = LogManager.getLogger(PropertyHandler.class);
    private static PropertyHandler instance = null;
    private Properties properties = null;

    private PropertyHandler() throws IOException {
        InputStream inputStream = null;
        try {
            properties = new java.util.Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream("config/" + propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public static synchronized PropertyHandler getInstance() throws IOException {
        if (instance == null)
            instance = new PropertyHandler();
        return instance;
    }

    @Override
    public String getValue(String propKey){
        return this.properties.getProperty(propKey);
    }

}