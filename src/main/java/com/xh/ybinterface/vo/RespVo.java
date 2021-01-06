package com.xh.ybinterface.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RespVo {
    private Integer code;
    private String message;
    private String type;
    private String data;

}
