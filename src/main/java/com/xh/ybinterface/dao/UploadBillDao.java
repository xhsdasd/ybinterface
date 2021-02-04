package com.xh.ybinterface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.ybinterface.to.UploadBillDTO;
import com.xh.ybinterface.vo.MatchItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UploadBillDao extends BaseMapper<UploadBillDTO> {


    UploadBillDTO getBillParm(@Param("caozy") String caozy, @Param("orgid") String orgid, @Param("type") String type);

    void updateMatch(@Param("data") List<MatchItem> data);


}