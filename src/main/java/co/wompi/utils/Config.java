package co.wompi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = Config.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new IllegalStateException("No se encontró el archivo config.properties");
            }

            PROPERTIES.load(inputStream);

        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible cargar config.properties", exception);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String propertyValue = PROPERTIES.getProperty(key);

        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalStateException("No se encontró la propiedad: " + key);
        }

        return propertyValue;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}