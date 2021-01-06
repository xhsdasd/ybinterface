package com.xh.ybinterface.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.ybinterface.to.BuyMedicineDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface BuyMedicineDao extends BaseMapper<BuyMedicineDTO> {


    BuyMedicineDTO getBuyMedicineTo(@Param("caozy") String caozy, @Param("orgid") String orgid, @Param("paidinamt") BigDecimal paidinamt);
}