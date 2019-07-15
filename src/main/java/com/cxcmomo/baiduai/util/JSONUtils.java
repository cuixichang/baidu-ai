package com.cxcmomo.baiduai.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 * 此类是将对象转换才JOSN的工具类
 * 
 * @author Keifer
 * @version [v1.0, 2014-1-25]
 */
public class JSONUtils {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    /**
     * 
     * 将简单对象转换成JSON字符串，最外层不需要KEY时使用
     * 
     * @param object
     *            入参
     * @return String
     */
    public static String convertToJSON(Object object) {
        String jsonString = null;
        Gson gson;
        if (object != null) {
            try {
                gson = new GsonBuilder().create();
                jsonString = gson.toJson(object);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex.getCause());
                throw ex;
            }
        }
        return jsonString;
    }

    /**
     * 将json字符串转换成简单对象
     * 
     * @param <T>
     *            泛型
     * @param jsonString
     *            入参
     * @param clazz
     *            入参
     * @return T
     */
    public static <T> T convertFromJSON(String jsonString, Class<T> clazz) {
        if (jsonString !=null && jsonString.length()>0) {
            try {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonString, clazz);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex.getCause());
                throw ex;
            }
        }
        return null;
    }

    /**
     * 将json字符串转换成 对象数组
     * 
     * @author 伍鹏慧
     * @param <T>
     *            泛型
     * @param jsonString
     *            入参
     * @return
     * @since JDK 1.7.0
     */
    public static <T> List<T> convertToList(String jsonString, Class<T> cls) {
        try {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());//把JSON格式的字符串转为List  
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            throw ex;
        }
    }

    /**
     * 把一个json的字符串转换成为一个包含POJO对象的List
     *
     * @param string
     *            [{"name":"l","age":10},{"name":"2","age":20}]
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonStringConvertToList(String string, Class<T[]> cls) {
        try {
            Gson gson = new GsonBuilder().create();
            T[] array = gson.fromJson(string, cls);
            return Arrays.asList(array);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
            throw ex;
        }
    }

    /**
     * listKeyMaps:(将json字符串转成Map). <br/>
     *
     * @author 伍鹏慧-pc
     * @param jsonString
     * @return
     * @since JDK 1.7.0
     */
    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            throw e;
        }
        return list;
    }

    /**
     *
     * 函数名称: convertToMapObject <br/>
     * 函数描述: 将json字符串转换为Map<String, Object>
     * 
     * @param jsonString
     * @return
     */
    public static Map<String, Object> convertToMapObject(String jsonString) {
        Map<String, Object> map = null;
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson g = gb.create();
            map = g.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            throw e;
        }
        return map;
    }

    /**
     *
     * 函数名称: convertToMapString <br/>
     * 函数描述: 将json字符串转换为Map<String, String>
     * 
     * @param jsonString
     * @return
     */
    public static Map<String, String> convertToMapString(String jsonString) {
        Map<String, String> map = null;
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson g = gb.create();
            map = g.fromJson(jsonString, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            throw e;
        }
        return map;
    }
}
