package com.xh.ybinterface.service;

import java.math.BigDecimal;

public interface UploadService {

    void uploadBuy(String caozy, String orgid, BigDecimal paidinamt);

    void uploadGoodStore(String caozy, String orgid);

    void uploadBill(String caozy, String orgid, Integer i);
}
