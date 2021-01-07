package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class StoreChangeDetailDTO {
    private String medListCodg; //医疗目录编码
    private String medListName; //医疗目录名称
    private String invChgType; //库存变更类型
    private String medinsListCodg; //医药机构目录编码
    private String medinsListName; //医药机构目录名称
    private BigDecimal pric; //单价
    private BigDecimal cnt; //数量
    private String rxFlag; //处方药标志
    private String invChgTime; //库存变更时间

}
