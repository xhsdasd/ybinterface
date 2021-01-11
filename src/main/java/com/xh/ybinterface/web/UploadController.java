package com.xh.ybinterface.web;

import com.xh.ybinterface.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //        1		DSS_DSM_00003	上传药店待结购药信息（新接口）	/DSS_DSM_00003	POST	DocumentUploadDTO	无	DSS_DSM_00003
//        2		DSS_DSM_00008	上传商品盘存信息	/DSS_DSM_00008	POST
//        3		DSS_DSM_00009	上传单据信息	/DSS_DSM_00009	POST
//        4		DSS_DSM_00010	上传商品的库存变更信息	/DSS_DSM_00010	POST
//        5		DSS_DSM_00011	删除商品信息	/DSS_DSM_00011	POST
    /**
     * DSS_DSM_00003	上传药店待结购药信息（新接口）
     * @param caozy
     * @param orgid
     * @param paidinamt
     */
    @GetMapping("sendBuy")
    public  void send(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("paidinamt") BigDecimal paidinamt){

       uploadService.uploadBuy(caozy,orgid,paidinamt);

    }

    /**
     * DSS_DSM_00008	上传商品盘存信息	/DSS_DSM_00008
     * @param caozy
     * @param orgid
     * @param type  0表示初装，1非初装
     */
    @GetMapping("goodstore")
    public void goodstore(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("type") String type){
        uploadService.uploadGoodStore(caozy,orgid,type);
    }

    /**
     * DSS_DSM_00009	上传单据信息	/DSS_DSM_00009
     * @param caozy
     * @param orgid
     * @param type
     */
    @GetMapping("uploadBill")
    public void uploadBill(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("type")String type){
//        1	采购入库	4	销售
//        2	其他入库	5	销售退货
//        3	采购退货	6	其他出库

uploadService.uploadBill(caozy,orgid,type);

    }

    /**
     * DSS_DSM_00010	上传商品的库存变更信息	/DSS_DSM_00010
     * @param caozy
     * @param orgid
     * @param type
     */
    //    public void changeStore(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("type")String type){

    @GetMapping("changeStore")
    public void changeStore(@RequestParam("date")String date){
//        101	调拨入库	105	销毁
//        102	调拨出库	106	其他入库
//        103	盘盈	107	其他出库
//        104	盘损	108	初始化入库

      uploadService.uploadChangeStore(date);

    }

    @GetMapping("test")
    public void test(@RequestParam("dates")String dates) {
        uploadService.test(dates);
    }
}
