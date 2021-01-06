package com.xh.ybinterface.web;

import com.xh.ybinterface.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
     */
    @GetMapping("goodstore")
    public void goodstore(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid){
        uploadService.uploadGoodStore(caozy,orgid);
        uploadService.uploadBill(caozy,orgid,1);
    }
    @GetMapping("uploadBill")
    public void uploadBill(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("type")Integer type){
//        1	采购入库	4	销售
//        2	其他入库	5	销售退货
//        3	采购退货	6	其他出库
uploadService.uploadBill(caozy,orgid,type);

    }

    @GetMapping("test")
    public void test(@RequestParam("caozy")String caozy,@RequestParam("orgid")String orgid,@RequestParam("paidinamt") BigDecimal paidinamt) {
        System.out.println(1);
    }
}
