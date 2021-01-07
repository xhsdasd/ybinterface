package com.xh.ybinterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.xh.ybinterface.dao.BuyMedicineDao;
import com.xh.ybinterface.dao.GoodStoreDao;
import com.xh.ybinterface.dao.UploadBillDao;
import com.xh.ybinterface.service.UploadService;
import com.xh.ybinterface.to.*;
import com.xh.ybinterface.vo.ConfigVo;
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
import org.springframework.beans.BeanUtils;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadServiceImp implements UploadService {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    ConfigVo configVo;
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
        BeanUtils.copyProperties(configVo, paramDTO);
        try {
            log.info("上传购药信息(单据编号:" + paramDTO.getBuymedDocCodg() + ")，----信息：" + paramDTO);
            commSend("DSS_DSM_00003", JSON.toJSONString(paramDTO), 1);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传购药信息(单据编号:" + paramDTO.getBuymedDocCodg() + ")错误，----错误信息：" + e.getMessage());
        }
    }

    /**
     * 2		DSS_DSM_00008	上传商品盘存信息
     *
     * @param caozy
     * @param orgid
     */
    @Override
    public void uploadGoodStore(String caozy, String orgid) {
        GoodStoreDTO paramDTO = goodStoreDao.getGoodStore(caozy, orgid);
        BeanUtils.copyProperties(configVo, paramDTO);
        //盘损
        List<GoodStoreDetailDTO> beforeZeroList = paramDTO.getDrugInventoryList().stream().filter(d -> d.getAlnum() > 0).collect(Collectors.toList());
        //盘盈
        List<GoodStoreDetailDTO> afterZeroList = paramDTO.getDrugInventoryList().stream().filter(d -> d.getAlnum() < 0).collect(Collectors.toList());
//        103	盘盈
//        104	盘损 上传库存变更
        List<StoreChangeDetailDTO> beforeDList = beforeZeroList.parallelStream().map(d -> {
            StoreChangeDetailDTO storeChangeDetailDTO = new StoreChangeDetailDTO();
            BeanUtils.copyProperties(d, storeChangeDetailDTO);
            storeChangeDetailDTO.setInvChgType("104");
            storeChangeDetailDTO.setInvChgTime(d.getInvDate());
            storeChangeDetailDTO.setCnt(new BigDecimal(Math.abs(d.getAlnum())));

            return storeChangeDetailDTO;
        }).collect(Collectors.toList());

        List<StoreChangeDetailDTO> afterDList = afterZeroList.parallelStream().map(d -> {
            StoreChangeDetailDTO storeChangeDetailDTO = new StoreChangeDetailDTO();
            BeanUtils.copyProperties(d, storeChangeDetailDTO);
            storeChangeDetailDTO.setInvChgType("103");
            storeChangeDetailDTO.setInvChgTime(d.getInvDate());
            storeChangeDetailDTO.setCnt(new BigDecimal(d.getAlnum()));

            return storeChangeDetailDTO;
        }).collect(Collectors.toList());

        if (beforeDList != null && beforeDList.size() > 0) {
            StoreChangeDTO storeChangeDTO = new StoreChangeDTO();
            BeanUtils.copyProperties(paramDTO, storeChangeDTO);
            storeChangeDTO.setDrugProdInvChgList(beforeDList);
            uploadStoreChange(storeChangeDTO);
        }

        if (afterDList != null && afterDList.size() > 0) {
            StoreChangeDTO storeChangeDTO = new StoreChangeDTO();
            BeanUtils.copyProperties(paramDTO, storeChangeDTO);
            storeChangeDTO.setDrugProdInvChgList(afterDList);
            uploadStoreChange(storeChangeDTO);
        }


        try {
            log.info("上传商品盘存信息(单据编号:" + paramDTO.getBillcode() + ")，----信息：" + paramDTO);
            commSend("DSS_DSM_00008", JSON.toJSONString(paramDTO), 2);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传商品盘存信息(单据编号:" + paramDTO.getBillcode() + ")，----错误信息：" + e.getMessage());
        }
    }

    /**
     * 3		DSS_DSM_00009	上传单据信息	/DSS_DSM_00009	POST
     * //        1	采购入库	4	销售
     * //        2	其他入库	5	销售退货
     * //        3	采购退货	6	其他出库
     *
     * @param caozy
     * @param orgid
     * @param type
     */
    @Override
    public void uploadBill(String caozy, String orgid, String type) {
        UploadBillDTO paramDTO = uploadBillDao.getBillParm(caozy, orgid, type);
        BeanUtils.copyProperties(configVo, paramDTO);

        List<StoreChangeDetailDTO> storeDlist = paramDTO.getDrugReciptList().stream().map(d -> {
            StoreChangeDetailDTO storeChangeDetailDTO = new StoreChangeDetailDTO();
//            if (d.getIsPurOk().equals("N")) {
//                //首次采购需要上传库存变更
//                BeanUtils.copyProperties(d, storeChangeDetailDTO);
//                storeChangeDetailDTO.setInvChgType("108");
//                storeChangeDetailDTO.setPric(d.getFinlTrnsPric());
//                storeChangeDetailDTO.setCnt(d.getPurcRetnCnt());
//                storeChangeDetailDTO.setInvChgTime(d.getPurcRetnStoinTime());
//                return storeChangeDetailDTO;
//            }
            BeanUtils.copyProperties(d, storeChangeDetailDTO);
            if (d.getIsPurOk().equals("N")) {
                //首次采购
                storeChangeDetailDTO.setInvChgType("108");
            } else {
                if (type.equals("1")) {
                    storeChangeDetailDTO.setInvChgType("101");
                }
                if (type.equals("3")) {
                    storeChangeDetailDTO.setInvChgType("102");
                }
            }
            storeChangeDetailDTO.setPric(d.getFinlTrnsPric());
            storeChangeDetailDTO.setCnt(d.getPurcRetnCnt());
            storeChangeDetailDTO.setInvChgTime(d.getPurcRetnStoinTime());
            return storeChangeDetailDTO;

        }).collect(Collectors.toList());
        //上传108	初始化入库 库存变更
        if (storeDlist != null && storeDlist.size() > 0 && storeDlist.get(0) != null) {
            StoreChangeDTO storeChangeDTO = new StoreChangeDTO();
            BeanUtils.copyProperties(paramDTO, storeChangeDTO);
            storeChangeDTO.setDrugProdInvChgList(storeDlist);
            uploadStoreChange(storeChangeDTO);
        }

        try {
            log.info("上传单据信息(类型" + type + ";单据编号(" + paramDTO.getDrugReciptList().get(0).getDyntNo() + "))，----信息：" + paramDTO);
            commSend("DSS_DSM_00009", JSON.toJSONString(paramDTO), 3);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传单据信息(类型" + type + ";单据编号(" + paramDTO.getDrugReciptList().get(0).getDyntNo() + "))，----错误信息：" + e.getMessage());
        }
    }


    /**
     * 4		DSS_DSM_00010	上传商品的库存变更信息
     * //        101	调拨入库	105	销毁
     * //        102	调拨出库	106	其他入库
     * //        103	盘盈	107	其他出库
     * //        104	盘损	108	初始化入库
     *
     * @param paramDTO
     */
    public void uploadStoreChange(StoreChangeDTO paramDTO) {
        try {
            log.info("上传库存变更信息,类型(" + paramDTO.getDrugProdInvChgList().get(0).getInvChgType() + ")，----信息：" + paramDTO);
            commSend("DSS_DSM_00010", JSON.toJSONString(paramDTO), 4);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传库存变更信息,类型(" + paramDTO.getDrugProdInvChgList().get(0).getInvChgType() + ")，----错误信息：" + e.getMessage());
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
        log.info("【接口返回结果-result】" + result);
        RespVo respVo = JSON.parseObject(result, RespVo.class);
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
                    log.error("上传库存信息变更错误：errOr:" + result);
                    break;
                case 5:
                    break;
            }

        }

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
