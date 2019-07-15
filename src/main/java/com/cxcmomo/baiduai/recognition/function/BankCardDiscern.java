package com.cxcmomo.baiduai.recognition.function;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.BaiDuAIDiscern;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 银行卡识别接口
 * ClassName:BankCardDiscern <br/>
 * Date: 2019/7/8 13:21 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class BankCardDiscern extends BaiDuAIDiscern {

    public BankCardDiscern(AipOcr aipOcr) {
        super(aipOcr);
    }

    @Override
    public String discernByIO(byte[] buffer, Map<String, String> expand) throws Exception {
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.bankcard(buffer, options);
        return this.orgStructure(jsonObject,"bankcard.field.front");
    }

    @Override
    public String discernByURL(String localUrl, Map<String, String> expand) throws Exception {
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject jsonObject = aipOcr.bankcard(localUrl, options);
        return this.orgStructure(jsonObject,"bankcard.field.front");
    }

    @Override
    public void help(){
        super.help();
        StringBuffer stringBuffer = new StringBuffer("EXPLAN :银行卡识别接口说明");
        stringBuffer.append("\n");
        stringBuffer.append("(无拓展传参)");
        stringBuffer.append("\n");
        stringBuffer.append("===================== end  =======================");
        System.out.println(stringBuffer.toString());
    }
}
