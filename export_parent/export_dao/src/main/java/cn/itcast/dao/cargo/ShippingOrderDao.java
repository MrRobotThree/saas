package cn.itcast.dao.cargo;


import cn.itcast.domain.cargo.ShippingOrder;

public interface ShippingOrderDao {
    int deleteByPrimaryKey(String shippingOrderId);

    int insert(ShippingOrder record);

    int insertSelective(ShippingOrder record);

    ShippingOrder selectByPrimaryKey(String shippingOrderId);

    int updateByPrimaryKeySelective(ShippingOrder record);

    int updateByPrimaryKey(ShippingOrder record);
}