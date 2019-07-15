package com.cxcmomo.baiduai;

import com.baidu.aip.ocr.AipOcr;
import com.cxcmomo.baiduai.recognition.BaiDuAIDiscern;
import com.cxcmomo.baiduai.recognition.function.BankCardDiscern;
import com.cxcmomo.baiduai.recognition.function.BusinessLicenseDiscern;
import com.cxcmomo.baiduai.recognition.function.CustomOrganizationCodeDiscern;
import com.cxcmomo.baiduai.recognition.function.IdCardDiscern;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:BaiDuDemo01 <br/>
 * Date: 2019/7/8 13:03 <br/>
 *
 * @author 崔希昌
 * @version 1.0
 * @see
 * @since JDK 1.8.0
 */
public class BaiDuDemo01 {
    public static void main(String[] args){
        String appId = "appId";
        String appKey = "appKey";
        String secretKey = "secretKey";
        BaiDuAIDiscern baiDuAIDiscern = new BusinessLicenseDiscern(new AipOcr(appId,appKey,secretKey));
        BaiDuAIDiscern baiDuAIDiscern2 = new BankCardDiscern(new AipOcr(appId,appKey,secretKey));
        BaiDuAIDiscern baiDuAIDiscern3 = new IdCardDiscern(new AipOcr(appId,appKey,secretKey));
        BaiDuAIDiscern baiDuAIDiscern4 = new CustomOrganizationCodeDiscern(new AipOcr(appId,appKey,secretKey));
        Map map = new HashMap();
        map.put("templateSign","templateSign");
        try{
           // String s = baiDuAIDiscern.discernByURL("H:\\临时存放文件与数据\\测试图片\\1234.jpg",null);
          //  String s = baiDuAIDiscern3.discernByURL("H:\\临时存放文件与数据\\测试图片\\12.jpg",null);
           // String s = baiDuAIDiscern2.discernByURL("H:\\临时存放文件与数据\\测试图片\\223.jpg",null);
            String s = baiDuAIDiscern4.discernByURL("H:\\临时存放文件与数据\\测试图片\\444.jpg",map);
            System.out.println(s);
        }catch(FileNotFoundException e){
            System.out.println("文件未发现");
        }catch (Exception e2){
            System.out.println("解析异常，请重试");
        }
        baiDuAIDiscern4.help();

    }
}
