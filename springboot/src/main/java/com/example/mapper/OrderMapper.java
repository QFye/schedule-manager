package com.example.mapper;

import com.example.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作order相关数据接口
*/
public interface OrderMapper {

    /**
      * 新增
    */
    int insert(Order order);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Order order);

    /**
      * 根据ID查询
    */
    Order selectById(Integer id);

    /**
      * 查询所有
    */
    List<Order> selectAll(Order order);

    @Select("select order.id as id, user.name as user, business.name as business, business.phone as phone, order.details as details, order.price as price,order.date as date, event.name as event from `order`,user,business,event where order.userId = user.id and order.eventId = event.id and event.businessId = business.id and user.id = #{id}")
    List<Order> selectByUserId(Integer id);

}