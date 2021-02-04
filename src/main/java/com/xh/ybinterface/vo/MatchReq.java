package com.xh.ybinterface.vo;

import lombok.Data;

@Data
public class MatchReq {
    private String authPhacCodg; //API药店授权编码(药店认证码)
    private String rtalPhacCodg; //零售药店编码
    private Integer pageNum=1; //分页页码
    private Integer pageSize=300; //分页大小
    private String matchDate; //匹配日期
    private String medinsListCodg; //药店目录编码

}
