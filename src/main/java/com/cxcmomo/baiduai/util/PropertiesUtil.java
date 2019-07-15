package com.cxcmomo.baiduai.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * ClassName:PropertiesUtil <br/>
 * Date: 2019/7/4 11:35 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class PropertiesUtil {

    //设置默认编码集
    private static final String location = "UTF-8";

    //文件根路径
    private static final String path = "";

    private static Properties getPropertiesObjByURL(String URL) throws IOException {
        Resource resource = new ClassPathResource(URL);
        Properties props = new Properties();
        PropertiesLoaderUtils.fillProperties(props, new EncodedResource(resource,location));
        return props;
    }

    private static Properties getPropertiesObj(String resourceName) throws IOException {
        String pathUrl = path + File.separator + resourceName;
        return getPropertiesObjByURL(pathUrl);
    }

    public static String getMessageByPropertiesFromRoot(String key, String resourceName) throws Exception {
        Properties props = getPropertiesObj(resourceName);
        String resource = props.getProperty(key);
        return  resource;
    }

    public static String resolverMessageByPropertiesFromRoot(String key, String resourceName, String... params) throws Exception {
        Properties props = getPropertiesObj(resourceName);
        String resource = props.getProperty(key);
        return  MessageFormat.format(resource,params);
    }
}
