package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class BuyMedicineDTO {
    private String rtalPhacCodg="test_4401";//零售药店编码
    private String rtalPhacName="对外测试机构(药店接口)";//零售药店名称
    private String buymedDocCodg;//购药单据编号
    private BigDecimal selAmt;//销售金额
    private String buymedDate;//购药日期
    private String authPhacCodg="test_4401A";//API授权药店编码
    private String tranIdSn;//药店交易识别码
    private String crterName;//录入人
    private List<BuyMedicineDetailDTO> documentUploadDetailList;//上传单据明细信息

}
