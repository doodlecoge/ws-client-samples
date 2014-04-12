package me.hch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Administrator on 14-4-12.
 */
public class WsdlUrlConf {
    private static final Map<String, String> urls = new HashMap<String, String>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(WsdlUrlConf.class.getResourceAsStream("/confidential/ws_loc.properties"));
        } catch (IOException e) {
            throw new WsClientException(e);
        }

        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            String value = properties.getProperty(name);
            urls.put(name, value);
        }
    }

    public static String getString(String name) {
        return urls.get(name);
    }

    public static Set<String> getNames() {
        return urls.keySet();
    }
}
