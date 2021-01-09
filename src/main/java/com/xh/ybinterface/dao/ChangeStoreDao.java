package com.xh.ybinterface.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.ybinterface.to.StoreChangeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChangeStoreDao extends BaseMapper<StoreChangeDTO> {


//     StoreChangeDTO getLXChangeStore(@Param("caozy") String caozy, @Param("orgid") String orgid);

     StoreChangeDTO getLXChangeStore(@Param("billno") String billno);

     void modifyYBflag(@Param("billcode") String billcode);

}