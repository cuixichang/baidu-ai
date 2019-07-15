package com.cxcmomo.baiduai.recognition.function;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.BaiDuAIDiscern;
import com.cxcmomo.baiduai.recognition.util.ResponseCode;
import com.cxcmomo.baiduai.util.JSONUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 营业执照识别
 * ClassName:BusinessLicenseDiscern <br/>
 * Date: 2019/7/8 13:32 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class BusinessLicenseDiscern extends BaiDuAIDiscern {

    public BusinessLicenseDiscern(AipOcr aipOcr) {
        super(aipOcr);
    }

    @Override
    public String discernByIO(byte[] buffer, Map<String, String> expand) throws Exception {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.businessLicense(buffer, options);
        return this.orgStructure(jsonObject,"businessLicense.field.front");
    }

    @Override
    public String discernByURL(String localUrl, Map<String, String> expand) throws Exception {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.businessLicense(localUrl, options);
        return this.orgStructure(jsonObject,"businessLicense.field.front");
    }

    @Override
    protected String orgStructure(JSONObject jsonObject, String fieldStr, String... params)throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            //1.检查公共状态是否存在
            response = checkEnable(jsonObject);
            if("0".equals(response.get("code"))){
                orgParam(response, jsonObject, fieldStr);
            }

            //处理返回参数中文"无"
            Map<String,String> param = (Map<String, String>) response.get(OBJ);
            if(param !=null){
                Set set = param.keySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    String val = param.get(key);
                    if("无".equals(val)){
                        param.put(key,"");
                    }
                }
            }
            response.put(OBJ,param);
        }catch(Exception e){
            logger.error(e.getMessage());
            response = ResponseCode.error("file parsing error");
        }
        return JSONUtils.convertToJSON(response);
    };

    @Override
    public void help(){
        super.help();
        StringBuffer stringBuffer = new StringBuffer("EXPLAN :营业执照识别");
        stringBuffer.append("\n");
        stringBuffer.append("(无拓展传参)");
        stringBuffer.append("\n");
        stringBuffer.append("===================== end  =======================");
        System.out.println(stringBuffer.toString());
    }
}
