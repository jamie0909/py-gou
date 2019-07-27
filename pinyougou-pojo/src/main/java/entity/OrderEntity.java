package entity;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;

import java.io.Serializable;
import java.util.List;

public class OrderEntity implements Serializable {
    private List<TbOrderItem> orderItem;
    private TbOrder order;
    private TbPayLog payLog;
    private List<TbOrder> orderList;

    public TbPayLog getPayLog() {
        return payLog;
    }

    public void setPayLog(TbPayLog payLog) {
        this.payLog = payLog;
    }

    public List<TbOrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<TbOrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public List<TbOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TbOrder> orderList) {
        this.orderList = orderList;
    }
}
