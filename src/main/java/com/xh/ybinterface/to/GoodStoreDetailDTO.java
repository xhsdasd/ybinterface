package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class GoodStoreDetailDTO {
    private String medListCodg;//医疗目录编码
    private String medListName;//医疗目录名称
    private String medinsListCodg;//医药机构目录编码
    private String medinsListName;//医药机构目录名称
    private String rxFlag;//处方药标志
    private BigDecimal invCnt;//库存数量
    private String manuLotnum;//生产批号
    private String manuDate;//生产日期
    private String expyEnd;//有效期止
    private String invDate;//盘存日期
    private Integer alnum;//损溢数量
    private BigDecimal pric;//成本单价

}
