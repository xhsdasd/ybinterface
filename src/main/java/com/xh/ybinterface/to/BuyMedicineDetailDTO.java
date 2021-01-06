package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
@Data
public class BuyMedicineDetailDTO {
    private String medinsListCodg;//医药机构目录编码
    private BigDecimal rtalPric;//零售价格
    private BigDecimal selCnt;//销售数量
    private BigDecimal selAmt;//销售金额
    private String manuLotnum;//生产批号

}
