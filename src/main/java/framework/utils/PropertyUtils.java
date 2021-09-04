package framework.utils;

import framework.exception.LaboratoryFrameworkException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils() {
    }

    /**
     * @param pathInResources - path in classpath resources directory, must start with {@link java.io.File#separator}
     * @throws LaboratoryFrameworkException if:
     *                                      <ul>
     *                                          <li>Specified path is null</li>
     *                                          <li>InputStream for resource is null</li>
     *                                          <li>Any IOException has occurred</li>
     *                                      </ul>
     */
    public static Properties readFromFile(String pathInResources) throws LaboratoryFrameworkException {
        ValidationUtils.requireNotEmpty(pathInResources);
        if (!pathInResources.startsWith(File.separator)) {
            pathInResources = File.separator.concat(pathInResources);
        }
        try (InputStream resourceAsStream = PropertyUtils.class.getResourceAsStream(pathInResources)) {
            String message = String.format("InputStream for resource %s is null", pathInResources);
            ValidationUtils.requireNonNull(resourceAsStream, message);
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(resourceAsStream)) {
                Properties out = new Properties();
                out.load(bufferedInputStream);
                return out;
            }
        } catch (IOException e) {
            throw new LaboratoryFrameworkException(e);
        }
    }

}
