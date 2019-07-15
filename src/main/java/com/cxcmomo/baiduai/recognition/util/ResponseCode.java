package com.cxcmomo.baiduai.recognition.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:ResponseCode <br/>
 * Date: 2019/7/16 1:09 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class ResponseCode extends HashMap<String,Object>{

    public ResponseCode(String code, String message) {
        put("code", code);
        put("message", message);
    }

    public static ResponseCode error(String msg) {
        return error("-1", msg);
    }

    public static ResponseCode error(String code, String msg) {
        return new ResponseCode(code,msg);
    }

    public static ResponseCode ok(String msg) {
        return new ResponseCode("0",msg);
    }

    @Override
    public ResponseCode put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
