package com.xh.ybinterface;

import com.xh.ybinterface.dao.UploadBillDao;
import com.xh.ybinterface.service.UploadService;
import com.xh.ybinterface.vo.ConfigVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YbinterfaceApplicationTests {
    @Autowired
    ConfigVo configVo;
    @Autowired
    UploadService service;
    @Test
    void contextLoads() {
        service.uploadBill("K2CUH45TGF5","O2F7DMABNBR","1");
    }

}
