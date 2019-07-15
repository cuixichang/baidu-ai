package com.cxcmomo.baiduai.recognition.function;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.BaiDuAIDiscern;
import com.cxcmomo.baiduai.recognition.util.ResponseCode;
import com.cxcmomo.baiduai.util.JSONUtils;
import com.cxcmomo.baiduai.util.PropertiesUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义模板识别组织机构代码证
 * 自定义模板比较特殊，
 * 一般结构与通用接口结构不同，
 * 视情况自定义组织解析处理
 * ClassName:CustomOrganizationCodeDiscern <br/>
 * Date: 2019/7/8 13:44 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class CustomOrganizationCodeDiscern  extends BaiDuAIDiscern {

    public CustomOrganizationCodeDiscern(AipOcr aipOcr) {
        super(aipOcr);
    }

    @Override
    public String discernByIO(byte[] buffer, Map<String, String> expand) throws Exception {
        if(expand == null){
            return JSONUtils.convertToJSON(ResponseCode.error("请求传入参数异常，请检查"));
        }
        String templateSign = expand.get("templateSign");
        if(templateSign == null || templateSign.length()==0){
            return JSONUtils.convertToJSON(ResponseCode.error("请求传入参数异常，请检查"));
        }
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.custom(buffer,templateSign, options);
        logger.debug("BaiDuAI Discern Json:"+jsonObject.toString());
        return this.orgStructure(jsonObject,"organizationCode.field.front","custom.field.status","custom.message.status");
    }

    @Override
    public String discernByURL(String localUrl, Map<String, String> expand) throws Exception {
        if(expand == null){
            return JSONUtils.convertToJSON(ResponseCode.error("请求传入参数异常，请检查"));
        }
        String templateSign = expand.get("templateSign");
        if(templateSign == null || templateSign.length()==0){
            return JSONUtils.convertToJSON(ResponseCode.error("请求传入参数异常，请检查"));
        }
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.custom(localUrl,templateSign, options);
        logger.debug("BaiDuAI Discern Json:"+jsonObject.toString());
        return this.orgStructure(jsonObject,"organizationCode.field.front","custom.field.status","custom.message.status");
    }

    @Override
    protected String orgStructure(JSONObject jsonObject, String fieldStr, String... params)throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            String statusStr = params[0];
            String statusMessage = params[1];
            String status = PropertiesUtil.getMessageByPropertiesFromRoot( statusStr,fileName);
            String reStatus = jsonObject.getString(status);
            String message = PropertiesUtil.getMessageByPropertiesFromRoot(statusMessage,fileName);
            List<Map> messages = JSONUtils.convertToList(message,Map.class);
            boolean index =  true;
            for(Map m:messages){
                String c = (String) m.get(PUBLIC_STATUS);
                if(reStatus.equals(c)){
                    index = false;
                    if ("0".equals(reStatus)) {
                        //值
                        JSONObject data = jsonObject.getJSONObject("data");
                        Map<String,Object> dataMap = JSONUtils.convertFromJSON(data.toString(),Map.class);
                        List<Map<String,String>> list = (List<Map<String,String>>) dataMap.get("ret");
                        //映射关系
                        String mappingParams = PropertiesUtil.getMessageByPropertiesFromRoot(fieldStr,fileName);
                        List<Map> listMapping = JSONUtils.convertToList(mappingParams,Map.class);
                        Map<String,String> map = new HashMap<>();
                        for(Map<String,String> val:list){
                            String name = val.get("word_name");
                            String value = val.get("word");
                            for(Map<String,String> mapping:listMapping){
                                if(mapping.get("value").equals(name)){
                                    map.put(mapping.get(MAPPING),value);
                                    break;
                                }
                            }
                        }
                        response.put(OBJ,map);
                    }else {
                        response = new ResponseCode((String) m.get("code"), (String) m.get("message"));
                    }
                    break;
                }
            }

            if(index){
                logger.error("图片识别异常:{}" ,jsonObject.toString());
                response =  ResponseCode.error("图片识别异常:" + jsonObject.toString());
            }
        }catch(Exception e){
            logger.error("图片识别异常:{}" ,e.getMessage());
            response =  ResponseCode.error(e.getMessage());
        }
        return JSONUtils.convertToJSON(response);
    };

    @Override
    public void help(){
        super.help();
        StringBuffer stringBuffer = new StringBuffer("EXPLAN :组织机构识别接口说明");
        stringBuffer.append("\n");
        stringBuffer.append("百度回馈结构说明:");
        stringBuffer.append("\n");
        stringBuffer.append("{\"error_msg\":\"\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\"data\":");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t{\"ret\":");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t[{\"probability\":");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t{\"average\":0.990093,\"min\":0.912158,\"variance\":7.59E-4},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"location\":{\"top\":241,\"left\":179,\"width\":186,\"height\":28},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"word_name\":\"代码\",\"word\":\"5544816-7\"");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t{\"probability\":{\"average\":0.979634,\"min\":0.807534,\"variance\":0.003355},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"location\":{\"top\":335,\"left\":169,\"width\":228,\"height\":39},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"word_name\":\"机构名称\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"word\":\"圳市明宇达智能设备有限公\"");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t{\"probability\":{\"average\":0.977423,\"min\":0.866196,\"variance\":0.002475},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"location\":{\"top\":462,\"left\":168,\"width\":249,\"height\":87},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"word_name\":\"地址\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t\t\"word\":\"东省深圳市龙华新区龙华清祥清湖工业园宝能科技园9栋A6楼G单位\"");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\t}],");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"templateSign\":\"da9fc9065ec598fa7899d5155e6dbf8d\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"templateName\":\"组织机构\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"scores\":1,");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"isStructured\":true,");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"logId\":\"156256620994478\",");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t\"clockwiseAngle\":0");
        stringBuffer.append("\n");
        stringBuffer.append("\t\t},");
        stringBuffer.append("\n");
        stringBuffer.append("\t\"error_code\":0");
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        stringBuffer.append("\n");
        stringBuffer.append("===================== end  =======================");
        System.out.println(stringBuffer.toString());
    }
}
