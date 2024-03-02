package com.example.mapper;

import com.example.entity.EventCategory;

import java.util.List;

/**
 * 操作EventCategory相关数据接口
*/
public interface EventCategoryMapper {

    /**
      * 新增
    */
    int insert(EventCategory eventCategory);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(EventCategory eventCategory);

    /**
      * 根据ID查询
    */
    EventCategory selectById(Integer id);

    /**
      * 查询所有
    */
    List<EventCategory> selectAll(EventCategory eventCategory);

}