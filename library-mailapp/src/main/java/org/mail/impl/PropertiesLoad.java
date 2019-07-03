package org.mail.impl;

import javax.inject.Named;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Named
public class PropertiesLoad {

    private Properties prop = new Properties();
    private InputStream input;


    public PropertiesLoad() {
        try {
            input = PropertiesLoad.class.getClassLoader().getResourceAsStream("mail.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load a properties file
    }

    String getProperty(String str) {
        return prop.getProperty(str);

    }
}
