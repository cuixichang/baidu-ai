package com.cxcmomo.baiduai.recognition.function;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.BaiDuAIDiscern;
import com.cxcmomo.baiduai.util.JSONUtils;
import com.cxcmomo.baiduai.util.PropertiesUtil;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:IdCardDiscern <br/>
 * Date: 2019/7/8 13:00 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class IdCardDiscern extends BaiDuAIDiscern {

    /**
     * 组织请求参数
     * @param expand
     * @return
     */
    private HashMap<String ,String> orgParam(Map<String ,String> expand){
        Map<String, String> options = new HashMap<String, String>();
        String direction = expand.get("direction");
        if(!"true".equals(direction)){ direction = "false";}
        String risk = expand.get("risk");
        if(!"true".equals(risk)){ risk = "false";}
        options.put("detect_direction", direction);
        options.put("detect_risk", risk);
        return (HashMap)options;
    }

    public IdCardDiscern(AipOcr aipOcr) {
        super(aipOcr);
    }

    @Override
    public String discernByIO(byte[] buffer, Map<String, String> expand) throws Exception {
        if(buffer == null){
            throw new FileNotFoundException();
        }
        if(expand == null){ expand=new HashMap<>();}

        HashMap<String, String> options = orgParam(expand);

        String isFront = expand.get("isFront");
        if(!"back".equals(isFront)){ isFront = "front";}
        //头像朝上
        String sign = "idcard.field.front";
        if(!"front".equals(isFront)){
            sign = "idcard.field.back";
        }

        JSONObject jsonObject = aipOcr.idcard(buffer,isFront, options);
        logger.debug("BaiDuAI Discern Json:"+jsonObject.toString());
        return this.orgStructure(jsonObject,sign,"idcard.field.status","idcard.message.status");
    }

    @Override
    public String discernByURL(String localUrl, Map<String, String> expand) throws Exception {
        if(localUrl == null || localUrl.length()==0){
            throw new FileNotFoundException();
        }

        if(expand == null){ expand=new HashMap<>();}

        HashMap<String, String> options = orgParam(expand);

        String isFront = expand.get("isFront");
        if(!"back".equals(isFront)){ isFront = "front";}
        //头像朝上
        String sign = "idcard.field.front";
        if(!"front".equals(isFront)){
            sign = "idcard.field.back";
        }
        JSONObject jsonObject  = aipOcr.idcard(localUrl,isFront, options);
        logger.debug("BaiDuAI Discern Json:"+jsonObject.toString());
        return this.orgStructure(jsonObject,sign,"idcard.field.status","idcard.message.status");
    }

    @Override
    protected String orgStructure(JSONObject jsonObject, String fieldStr, String... params) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        String fieldStatus = params[0];
        String messageStatus = params[1];
        try{
            //1.检查公共状态是否存在
            response = checkEnable(jsonObject);
            if("0".equals(response.get("code"))){


                  /*身份证存在状态字段，验证传入是否合规，其他功能无*/
                String status = PropertiesUtil.getMessageByPropertiesFromRoot( fieldStatus,fileName);
                String reStatus = jsonObject.getString(status);
                String messages = PropertiesUtil.getMessageByPropertiesFromRoot(messageStatus, fileName);
                List<Map> list = JSONUtils.convertToList(messages, Map.class);
                boolean igs = true;
                for (Map<String, String> map : list) {
                    if (map.get(PUBLIC_STATUS).equals(reStatus)) {
                        igs = false;
                        //存在有效数据
                        if ("0".equals(map.get("code"))) {
                            orgParam(response, jsonObject, fieldStr);
                        }else {
                            response = responseCode(map.get("code"),map.get("message"));
                        }
                        break;
                    }
                }

                if(igs){
                    response = errorResponseCode("未判定状态异常:" + jsonObject.toString());
                }

            }
        }catch(Exception e){
            response.put("code","-1");
            response.put("message",e.getMessage());
        }
        return JSONUtils.convertToJSON(response);
    }

    @Override
    public void help(){
        super.help();
        StringBuffer stringBuffer = new StringBuffer("EXPLAN :身份证识别接口说明");
        stringBuffer.append("\n");
        stringBuffer.append("expand拓展参数说明:");
        stringBuffer.append("\n");
        stringBuffer.append("isFront(识别身份证正反参数)：front 正面 / back 反面");
        stringBuffer.append("\n");
        stringBuffer.append("direction(是否检测正反朝向)：true / false ");
        stringBuffer.append("\n");
        stringBuffer.append("risk(是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能");
        stringBuffer.append("\n");
        stringBuffer.append("===================== end  =======================");
        System.out.println(stringBuffer.toString());
    }
}
