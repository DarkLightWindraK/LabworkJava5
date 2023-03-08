import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Injector {
    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    public Injector(String propertiesFileName) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(propertiesFileName);
        } catch (IOException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            if (inputStream == null) {
                throw new RuntimeException("Properties file not found: " + propertiesFileName);
            }
        }
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }

        for (String key : properties.stringPropertyNames()) {
            Class<?> interfaceClass = Class.forName(key);
            Class<?> implementationClass = Class.forName(properties.getProperty(key));
            Object instance = implementationClass.newInstance();
            dependencies.put(interfaceClass, instance);
        }
    }

    public <T> void inject(T object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                field.setAccessible(true);
                Object dependency = dependencies.get(field.getType());
                if (dependency != null) {
                    field.set(object, dependency);
                } else {
                    throw new RuntimeException("Cannot find dependency for " + field.getType());
                }
            }
        }
    }
}
