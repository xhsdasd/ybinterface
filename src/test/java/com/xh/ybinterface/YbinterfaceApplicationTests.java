package com.xh.ybinterface;

import com.xh.ybinterface.dao.UploadBillDao;
import com.xh.ybinterface.service.UploadService;
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
    @Test
    void contextLoads() {
//        service.uploadChangeStore("312485");

//        service.uploadBuy("K2CUH45TGF5","O2CUH459RUT",new BigDecimal(252));
//        service.uploadChangeStore("312686");
//        1(String), K2CUH45TGF5(String), O2CUH459RUT(String)
//        service.uploadBill("K2CUH45TGF5","O2CUH459RUT","1");
//        service.uploadGoodStore("K2CUH45TGF5","O2CUH459RUT","0");
        service.uploadChangeStore("");
    }

}
