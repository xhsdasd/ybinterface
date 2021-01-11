package com.xh.ybinterface.service;

import java.math.BigDecimal;

public interface UploadService {

    void uploadBuy(String caozy, String orgid, BigDecimal paidinamt);

    void uploadGoodStore(String caozy, String orgid, String type);

    void uploadBill(String caozy, String orgid, String type);

//    void uploadChangeStore(String caozy, String orgid);

    void uploadChangeStore(String date);

    void test(String dates);

}
