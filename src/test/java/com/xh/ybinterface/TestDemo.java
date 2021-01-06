package com.xh.ybinterface;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Tile: 测试接口Demo(以药店上传购药新接口为例)
 * @Author: chezhi
 * @Date:  2020-12-11
 * <p> Copyright: Copyright (c) 2020 <p>
 * <p> Powersi Inc. – Confidential and Proprietary <p>
 */
public class TestDemo {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////");
        long begin = System.currentTimeMillis();
        testGzydxjkTest();
        long end = System.currentTimeMillis();
        long date = (end - begin) / 1000;
        System.out.println("调用耗时时间：【" + date + "】秒");

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////");
    }


    private static void testGzydxjkTest() throws IOException {

        /**
         入参内容如下，调用接口时需要将json数据转成json字符串
         {   "poolareaNo":"440100",
             "admdvs":"440100",
             "rtalPhacCodg": "test_4401",
             "rtalPhacName": "对外测试机构(药店接口)",
             "crterName": "测试",
             "suppFlag": "0",
             "buymedDocCodg": "XS202012160001",
             "selAmt": 2.00,
             "buymedDate": "2020-12-10",
             "authPhacCodg": "test_4401A",
             "tranIdSn": "f29ed4e2-e82e-11ea-92a5-00505695a8d21",
             "documentUploadDetailList": [
                 {
                     "medinsListCodg": "202012088001",
                     "rtalPric": "1.00",
                     "selCnt": "1",
                     "selAmt": "1.00",
                     "manuLotnum": "202011A"
                 },
                 {
                 "medinsListCodg": "202012088002",
                 "rtalPric": "1.00",
                 "selCnt": "1",
                 "selAmt": "1.00",
                 "manuLotnum": "202011B"
                 }
             ]
         }
         */

        String currentDay = simpleDateFormat.format(new Date());

        //  测试数据   单据信息
        Map<String, Object> mapOrd = new HashMap<String, Object>();
        mapOrd.put("medinsListCodg", "8205512");   // 药店目录编码
        mapOrd.put("poolareaNo", "");   //
        mapOrd.put("admdvs", "440100");   //  药店所在行政区， 可以取医保区划的值
        mapOrd.put("admdvs", "440100");   //  药店所在医保区划， 在新系统的"药店基础信息维护"菜单中可以查询
        mapOrd.put("rtalPhacCodg", "test_4401");   //  药店编码， 在新系统的"药店基础信息维护"菜单中可以查询
        mapOrd.put("rtalPhacName", "对外测试机构(药店接口)");   //  药店名称， 在新系统的"药店基础信息维护"菜单中可以查询
        mapOrd.put("crterName", "测试");   //  收银员（经办人）
        String buymedDocCodg = "XS" + simpleDateFormat2.format(new Date()) + (int)(Math.random() * 100000);
        mapOrd.put("buymedDocCodg", buymedDocCodg);   //   药店收银系统中的购药单据号
        mapOrd.put("selAmt", "2.00");   //   药店收银系统中单据的总金额
        mapOrd.put("buymedDate", currentDay);   //   购药单据日期
        mapOrd.put("authPhacCodg", "test_4401A");   // 药店授权码， 在新系统的"药店基础信息维护"菜单中查看药店详情可以查询到，第二个信息展示页签有展示
        mapOrd.put("tranIdSn", UUID.randomUUID().toString());   //   收银系统中的流水号（唯一）

        //  测试数据   单据明细信息
        List<Map<String, Object>> listDetail = new ArrayList<>();
        Map<String, Object> mapDetail = new HashMap<String, Object>();
        mapDetail.put("medinsListCodg", "202012088001");   // 药店目录编码
        mapDetail.put("rtalPric", "1.00");    // 药店商品价格
        mapDetail.put("selCnt", "1");   // 药店商品销售数量
        mapDetail.put("selAmt", "1.00");  // 药店商品销售总价
        mapDetail.put("manuLotnum", "202011A");  // 药店商品批号
        listDetail.add(mapDetail);

        Map<String, Object> mapDetail2 = new HashMap<String, Object>();
        mapDetail2.put("medinsListCodg", "8115352");   // 药店目录编码
        mapDetail2.put("rtalPric", "1.00");    // 药店商品价格
        mapDetail2.put("selCnt", "1");   // 药店商品销售数量
        mapDetail2.put("selAmt", "1.00");  // 药店商品销售总价
        mapDetail2.put("manuLotnum", "202011A");  // 药店商品批号
        listDetail.add(mapDetail2);

        mapOrd.put("documentUploadDetailList", listDetail);

        //   将json转成json字符串
        String requestBody = JSON.toJSONString(mapOrd);
        String serverName = "DSS_DSM_00003";   //  上传购药待刷卡单据和明细信息， 接口文档中有定义， 这里以这个接口为例

        String accessKey = "JgYEaQgJQ7JZTh4ztGOkDbXik1Bs2LL0SAJwg5";   // 接口授权账号
        String secreKey = "SKtmWtHJZlfuLDiwmskWwqJ017Eaa9h03CEnqxka";   // 接口授权密码
        String uri = "http://112.94.67.148:20001/isp/ebus/gzyth/dss/" + serverName;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);  //  时间戳，单位秒级
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String signature = getSHA256Str(timestamp + secreKey + nonce + timestamp);
        CloseableHttpClient build = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(uri);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(60000).
                setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setRedirectsEnabled(Boolean.FALSE).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("x-tif-paasid", accessKey);
        httpPost.addHeader("x-tif-timestamp", timestamp);
        httpPost.addHeader("x-tif-nonce", nonce);
        httpPost.addHeader("x-tif-signature", signature);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(requestBody,"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse execute = build.execute(httpPost);
        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("【testRioGateWayDss-result】" + result);

    }

    private static String getSHA256Str(String str) {
        String encodeStr = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        String temp = "";
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}