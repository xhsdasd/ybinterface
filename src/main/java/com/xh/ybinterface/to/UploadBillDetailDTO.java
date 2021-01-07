package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class UploadBillDetailDTO {
    private String dyntNo; //随货单号
    private String purcInvoNo; //采购发票号
    private String splerName; //供货商名称
    private String medListCodg; //医疗目录编码
    private String medListName; //医疗目录名称
    private String medinsListCodg; //医药机构目录编码
    private String medinsListName; //医药机构目录名称
    private String manuLotnum; //生产批号
    private String manuDate; //生产日期
    private String expyEnd; //有效期止
    private String prodentpName; //生产厂家
    private String aprvno; //批准文号
    private String spec; //规格
    private String dosformType; //剂型
    private BigDecimal finlTrnsPric; //单价
    private String rxFlag; //处方药标志
    private BigDecimal purcRetnCnt; //采购/退货数量
    private String purcRetnStoinTime; //采购/退货入库时间
    private String purcRetnOpterName; //采购经办人姓名
    private String isPurOk; //首次供应完成


}
