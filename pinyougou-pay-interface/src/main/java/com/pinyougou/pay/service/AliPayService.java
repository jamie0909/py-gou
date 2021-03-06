package com.pinyougou.pay.service;

import java.util.Map;

public interface AliPayService {

    /**
     * 生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    public Map createNative(String out_trade_no, String total_fee);

    public Map queryPayStatus(String out_trade_no);
}
