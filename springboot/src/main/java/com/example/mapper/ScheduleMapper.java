package com.example.mapper;

import com.example.entity.Schedule;

import java.util.Date;
import java.util.List;

/**
 * 操作schedule相关数据接口
*/
public interface ScheduleMapper {

    /**
      * 新增
    */
    int insert(Schedule schedule);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Schedule schedule);

    /**
      * 根据ID查询
    */
    Schedule selectById(Integer id);

    /**
      * 查询所有
    */
    List<Schedule> selectAll(Schedule schedule);

    Schedule selectByIdAndDate(Integer id, Date date);
}