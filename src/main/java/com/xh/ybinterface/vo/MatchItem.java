package com.xh.ybinterface.vo;

import lombok.Data;

@Data
public class MatchItem {
        private String rtalPhacName; //零售药店名称
        private String medinsListType; //药店目录类别
        private String medinsListCodg; //药店目录编码
        private String medinsListName; //药店目录名称
        private String medListCodg; //医保目录编码
        private String medListName; //医保目录名称
        private String listType; //医保目录类别
        private String listTypeName; //医保目录类别名称
}
