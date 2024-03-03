package com.example.mapper;

import com.example.entity.ScheduleCategory;

import java.util.List;

/**
 * 操作ScheduleCategory相关数据接口
*/
public interface ScheduleCategoryMapper {

    /**
      * 新增
    */
    int insert(ScheduleCategory scheduleCategory);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(ScheduleCategory scheduleCategory);

    /**
      * 根据ID查询
    */
    ScheduleCategory selectById(Integer id);

    /**
      * 查询所有
    */
    List<ScheduleCategory> selectAll(ScheduleCategory scheduleCategory);

}