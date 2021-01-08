package com.xh.ybinterface.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.ybinterface.to.GoodStoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodStoreDao extends BaseMapper<GoodStoreDTO> {


     GoodStoreDTO getGoodStore(@Param("caozy") String caozy, @Param("orgid") String orgid);

     GoodStoreDTO getFirstGoodStore(@Param("orgid") String orgid);

}