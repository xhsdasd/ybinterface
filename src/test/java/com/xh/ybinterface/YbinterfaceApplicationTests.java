package com.xh.ybinterface;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xh.ybinterface.dao.BuyMedicineDao;
import com.xh.ybinterface.dao.UploadBillDao;
import com.xh.ybinterface.service.UploadService;
import com.xh.ybinterface.to.BuyMedicineDTO;
import com.xh.ybinterface.vo.ConfigVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class YbinterfaceApplicationTests {
    @Autowired
    ConfigVo configVo;
    @Autowired
    UploadService service;
    @Autowired
    BuyMedicineDao buyMedicineDao;
    @Test
    void contextLoads() {
//        service.uploadChangeStore("312485");

//        service.uploadBuy("K2CUH45TGF5","O2CUH459RUT",new BigDecimal(252));
        service.uploadChangeStore("2021-01-12");
//        1(String), K2CUH45TGF5(String), O2CUH459RUT(String)
//        service.uploadBill("K2CUH45TGF5","O2CUH459RUT","1");
//        service.uploadGoodStore("K2CUH45TGF5","O2CUH459RUT","0");
//        Integer buymedDate = buyMedicineDao.selectCount(new QueryWrapper<BuyMedicineDTO>().eq("buymedDate", "2020-01-11"));
//        System.out.println(buymedDate);
    }

}
