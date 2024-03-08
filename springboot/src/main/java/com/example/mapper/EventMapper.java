package com.example.mapper;

import com.example.entity.Event;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 操作event相关数据接口
*/
public interface EventMapper {

    @Select("select * from event where categoryId = #{id}")
    List<Event> selectByCategoryId(Integer id);

    /**
      * 新增
    */
    int insert(Event event);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Event event);

    /**
      * 根据ID查询
    */
    Event selectById(Integer id);

    /**
      * 查询所有
    */
    List<Event> selectAll(Event event);

    @Select("select * from event,user,schedule,scheduleeventrelation where user.id=schedule.userId and schedule.id = scheduleeventrelation.scheduleId and event.id = scheduleeventrelation.eventId and user.id=#{id}")
    List<Event> selectByUserAndDate(Integer id);

    @Select("select * from event order by count desc limit 10")
    List<Event> selectTop10();

    @Select("select * from event where businessId = #{id}")
    List<Event> selectByBusinessId(Integer id);
}