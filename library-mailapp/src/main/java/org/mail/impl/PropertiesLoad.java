package org.mail.impl;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Named
public class PropertiesLoad {

    private Properties prop = new Properties();
    private InputStream input;


    public PropertiesLoad() throws IOException {
        input = PropertiesLoad.class.getClassLoader().getResourceAsStream("mail.properties");
        prop.load(input);
        input = PropertiesLoad.class.getClassLoader().getResourceAsStream("log4j.properties");
        prop.load(input);

    }

    String getProperty(String str) {
        return prop.getProperty(str);

    }
}
