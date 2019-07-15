package com.cxcmomo.baiduai.recognition;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.util.BCConvert;
import com.cxcmomo.baiduai.util.JSONUtils;
import com.cxcmomo.baiduai.util.PropertiesUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:BaiDuAIDiscern <br/>
 * Date: 2019/7/8 12:54 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public abstract class BaiDuAIDiscern {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String fileName = "baiduAINew.properties";

    /**
     * 属性文件中获取json层级KEY
     */
    protected static final String PUBLIC_ADDRESS = "address";

    protected static final String PUBLIC_STATUS = "status";

    /**
     * 属性文件中的json层级分隔符
     */
    protected static final String CUTOFF = "/";

    /**
     * 字段非空标识
     */
    protected static final String NOTNULL = "notnull";

    /**
     * 是否转化中英文操作
     */
    protected static final String CONVERSION = "cn";

    /**
     * 属性文件中获取对应映射结果的KEY
     */
    protected static final String MAPPING = "mapping";

    protected static final String OBJ = "obj";

    public AipOcr aipOcr;

    public BaiDuAIDiscern( AipOcr aipOcr){
        aipOcr.setConnectionTimeoutInMillis(5000);
        aipOcr.setSocketTimeoutInMillis(5000);
        this.aipOcr = aipOcr;
    }

    public abstract String discernByIO(byte[] buffer, Map<String ,String> expand) throws Exception;

    public abstract String discernByURL(String localUrl, Map<String ,String> expand) throws Exception;

    public Map<String, Object> errorResponseCode(String message){
        return responseCode("-1",message);
    }

    public Map<String, Object> responseCode(String code, String message){
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("code",code);
        response.put("message",message);
        return response;
    }

    protected String orgStructure(JSONObject jsonObject, String fieldStr, String... params)throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            //1.检查公共状态是否存在
            response = checkEnable(jsonObject);
            if("0".equals(response.get("code"))){
                orgParam(response, jsonObject, fieldStr);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            response = errorResponseCode("file parsing error");
        }
        return JSONUtils.convertToJSON(response);
    };

    /**
     * 检查接口是否可以正常请求
     * 因为百度接口可能返回2种报文（此接口公共检查错误报文是否存在）：
     * 1：错误性提示报文 2：正常报文信息
     * @return
     */
    protected Map<String,Object> checkEnable(JSONObject jsonObject) {
        String code = "0";
        String message = "file resolution passed";
        boolean isCheck = true;
        String status;
        try{
            status = PropertiesUtil.getMessageByPropertiesFromRoot( "public.field.status",fileName);
        }catch(Exception e){
            logger.error(e.getMessage());
            return errorResponseCode(e.getMessage());
        }

        String messages;
        try{
            messages = PropertiesUtil.getMessageByPropertiesFromRoot( "public.message.status",fileName);
        }catch(Exception e){
            logger.error(e.getMessage());
            return errorResponseCode(e.getMessage());
        }

        String reStatusStr;
        List<Map> list;
        try {
            reStatusStr = jsonObject.get(status).toString();
            list = JSONUtils.convertToList(messages, Map.class);
        }catch(Exception e){
            return responseCode(code,message);
        }

        for (Map<String, String> map : list) {
            if (map.get(PUBLIC_STATUS).equals(reStatusStr)) {
                isCheck = false;
                code = map.get("code");
                message = map.get("message");
                break;
            }
        }

        if (isCheck) {
            code = "-1";
            message = reStatusStr;
        }

        return responseCode(code,message);
    }

    /**
     * 组织字段映射
     * @return
     */
    protected static void orgParam(Map<String, Object> res, JSONObject jsonObject,
                                   String fieldStr) throws Exception {
        String params = PropertiesUtil.getMessageByPropertiesFromRoot(fieldStr,fileName);
        List<Map> list = JSONUtils.convertToList(params,Map.class);
        Map<String,String> map = new HashMap<>();
        for(Map<String,String> l:list){
            String[] paramArray = l.get(PUBLIC_ADDRESS).split(CUTOFF);
            JSONObject jsonObjectT = jsonObject;
            for(int i=0;i<paramArray.length;i++){
                int index = i + 1;
                if(index == paramArray.length){
                    String param = jsonObjectT.get(paramArray[i]).toString();
                    String flag = l.get(NOTNULL);
                    if("1".equals(flag)){
                        if(param == null || param.length()==0){
                            throw new Exception("图片上传不正确");
                        }
                    }

                    String conversion = l.get(CONVERSION);
                    if("TRUE".equalsIgnoreCase(conversion)){
                          param = BCConvert.bj2qj(param);
                    }

                    map.put(l.get(MAPPING),param);
                }else {
                    jsonObjectT = jsonObjectT.getJSONObject(paramArray[i]);
                }
            }

        }
        res.put(OBJ,map);
    }

    public void help(){
        StringBuffer stringBuffer = new StringBuffer("=====================explan=======================");
        stringBuffer.append("\n");
        stringBuffer.append("CURRENT Class :"+this.getClass().getSimpleName());
        System.out.println(stringBuffer);
    };
}

