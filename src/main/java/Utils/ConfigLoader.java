package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("Utils/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Configuration file 'config.properties' not found in classpath under Utils/");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim().replace("\\", "/"); 
        }
        return null;
    }
}
