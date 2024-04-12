package com.example.mapper;

import com.example.entity.Event;
import com.example.entity.Schedule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 操作event相关数据接口
*/
public interface EventMapper {

    @Select("select * from event where categoryId = #{id} and status = 'STATIC'")
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

    @Select("select * from event where event.status = 'STATIC'")
    List<Event> selectAllSTATIC(Event event);

    @Select("select *, eventCategory.name as categoryName from event,user,schedule,scheduleeventrelation,eventCategory where event.categoryId = eventCategory.id and user.id=schedule.userId and schedule.id = scheduleeventrelation.scheduleId and event.id = scheduleeventrelation.eventId and user.id=#{userId} and schedule.date = #{searchDate} order by event.start")
    List<Event> selectByUserAndDate(@Param("userId") Integer id, @Param("searchDate") Date date);

    @Select("select * from event where event.status = 'STATIC' order by count desc limit 10")
    List<Event> selectTop10();

    @Select("select * from event where businessId = #{id} and status = 'STATIC'")
    List<Event> selectByBusinessId(Integer id);

    @Select("select *, eventCategory.name as categoryName from event,repository,eventCategory where event.categoryId = eventCategory.id and repository.userId = #{id} and event.id = repository.eventId and status = 'STATIC'")
    List<Event> selectPersonal(Integer id);

    @Insert("insert into repository (eventId, userId) VALUES (#{event.id}, #{userId})")
    int addRepository(Event event, Integer userId);

    @Insert("insert into ScheduleEventRelation (eventId, scheduleId) VALUES (#{event.id}, #{schedule.id})")
    void insertIntoSchedule(Event event, Schedule schedule);

    @Delete("delete from ScheduleEventRelation where eventId = #{id} and scheduleId = #{schedule.id}")
    void deleteInSchedule(Integer id, Schedule schedule);

    @Select("select event.id from event, repository where event.id = repository.eventId and event.id=#{eventId} and repository.userId = #{userId} and status = 'STATIC'")
    Event selectFromRepository(Integer eventId, Integer userId);


    List<Event> selectAllFromRepository(Event event, Integer repositoryUserId);
}