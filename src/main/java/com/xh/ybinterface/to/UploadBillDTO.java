package com.xh.ybinterface.to;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UploadBillDTO {
    private String dataCatrg;//数据类别
    private String authPhacCodg;//API授权药店编码(药店认证码)
    private String rtalPhacCodg;//医药机构编码
    private String rtalPhacName;//医药机构名称
    private String fixmedinsBchno;//定点医药机构批次流水号
   private String supnDocType;//监管单据类别
    private List<UploadBillDetailDTO> drugReciptList;//单据信息集合

}
