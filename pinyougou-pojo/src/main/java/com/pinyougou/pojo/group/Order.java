package com.pinyougou.pojo.group;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;

public class Order implements Serializable {
    private TbOrder tbOrder;
    private TbOrderItem tbOrderItem;

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    public TbOrderItem getTbOrderItem() {
        return tbOrderItem;
    }

    public void setTbOrderItem(TbOrderItem tbOrderItem) {
        this.tbOrderItem = tbOrderItem;
    }
}
