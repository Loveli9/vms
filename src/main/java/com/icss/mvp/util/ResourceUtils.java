package com.icss.mvp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

//import org.apache.commons.io.IOUtils;

/**
 * Created by Ray on 2018/5/4.
 */
@Deprecated
public class ResourceUtils {

    private static Logger logger = Logger.getLogger(ResourceUtils.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public ResourceUtils(String... resourcesPaths) {
        properties = loadProperties(resourcesPaths);
    }

    private Properties loadProperties(String... resourcesPaths) {
        Properties props = new Properties();

        for (String location : resourcesPaths) {

            InputStream stream = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                stream = resource.getInputStream();
                props.load(resource.getInputStream());
            } catch (IOException ex) {
				logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
		return props;
	}

	/**
	 * 取出Property，但以System的Property优先.
	 */
	public String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return properties.getProperty(key);
	}

}
