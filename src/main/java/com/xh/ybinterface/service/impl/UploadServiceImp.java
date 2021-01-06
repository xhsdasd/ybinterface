package com.xh.ybinterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.xh.ybinterface.dao.BuyMedicineDao;
import com.xh.ybinterface.dao.GoodStoreDao;
import com.xh.ybinterface.dao.UploadBillDao;
import com.xh.ybinterface.service.UploadService;
import com.xh.ybinterface.to.BuyMedicineDTO;
import com.xh.ybinterface.to.GoodStoreDTO;
import com.xh.ybinterface.to.UploadBillDTO;
import com.xh.ybinterface.vo.RespVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class UploadServiceImp implements UploadService {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private BuyMedicineDao buyMedicineDao;
    @Autowired
    private GoodStoreDao goodStoreDao;
    @Autowired
    private UploadBillDao uploadBillDao;

    //        1		DSS_DSM_00003	上传药店待结购药信息（新接口）	/DSS_DSM_00003	POST	DocumentUploadDTO	无	DSS_DSM_00003
//        2		DSS_DSM_00008	上传商品盘存信息	/DSS_DSM_00008	POST
//        3		DSS_DSM_00009	上传单据信息	/DSS_DSM_00009	POST
//        4		DSS_DSM_00010	上传商品的库存变更信息	/DSS_DSM_00010	POST
//        5		DSS_DSM_00011	删除商品信息	/DSS_DSM_00011	POST

    /**
     * 1  DSS_DSM_00003	上传药店待结购药信息（新接口）
     *
     * @param caozy
     * @param orgid
     * @param paidinamt
     */
    @Override
    public void uploadBuy(String caozy, String orgid, BigDecimal paidinamt) {

        BuyMedicineDTO paramDTO = buyMedicineDao.getBuyMedicineTo(caozy, orgid, paidinamt);
        try {
            log.info("上传购药信息，----信息：" + paramDTO);
            commSend("DSS_DSM_00003", JSON.toJSONString(paramDTO), 1);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传购药信息错误，----错误信息：" + e.getMessage());
        }
    }

    /**
     * 2		DSS_DSM_00008	上传商品盘存信息
     * @param caozy
     * @param orgid
     */
    @Override
    public void uploadGoodStore(String caozy, String orgid) {
        GoodStoreDTO paramDTO =goodStoreDao.getGoodStore(caozy, orgid);
        try {
            log.info("上传商品盘存信息，----信息：" + paramDTO);
            commSend("DSS_DSM_00008", JSON.toJSONString(paramDTO), 2);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传商品盘存信息，----错误信息：" + e.getMessage());
        }
    }

    /**
     * 3		DSS_DSM_00009	上传单据信息	/DSS_DSM_00009	POST
     * //        1	采购入库	4	销售
     * //        2	其他入库	5	销售退货
     * //        3	采购退货	6	其他出库
     * @param caozy
     * @param orgid
     * @param type
     */
    @Override
    public void uploadBill(String caozy, String orgid, Integer type) {
        UploadBillDTO paramDTO =uploadBillDao.getBillParm(caozy,orgid,type);
        try {
            log.info("上传单据信息(类型"+type+")，----信息：" + paramDTO);
            commSend("DSS_DSM_00009", JSON.toJSONString(paramDTO), 3);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传单据信息(类型"+type+")，----错误信息：" + e.getMessage());
        }
    }


    /**
     * @param serverName  服务名
     * @param requestBody 参数json字符串
     */
    public void commSend(String serverName, String requestBody, int type) throws IOException {
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
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse execute = build.execute(httpPost);
        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
//    System.out.println("【testRioGateWayDss-result】" + result);
        RespVo respVo = JSON.parseObject(result,RespVo.class);
        if (respVo.getCode() != 0) {
            switch (type) {
                case 1:
                    log.error("上传购药信息错误：errOr:" + result);
                    break;
                case 2:
                    log.error("上传商品盘存信息错误：errOr:" + result);
                    break;
                case 3:
                    log.error("上传单据信息错误：errOr:" + result);
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }

        }

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
        String buymedDocCodg = "XS" + simpleDateFormat2.format(new Date()) + (int) (Math.random() * 100000);
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
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
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

    /**
     * 将一个类查询方式加入map（属性值为int型时，0时不加入，
     * 属性值为String型或Long时为null和“”不加入）
     * 注：需要转换的必须是对象，即有属性
     */
    public static Map<String, Object> setConditionMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();//获取类的各个属性值
        for (Field field : fields) {
            String fieldName = field.getName();//获取类的属性名称
            if (getValueByFieldName(fieldName, obj) != null)//获取类的属性名称对应的值
                map.put(fieldName, getValueByFieldName(fieldName, obj));
        }
        return map;
    }

    /**
     * 根据属性名获取该类此属性的值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
