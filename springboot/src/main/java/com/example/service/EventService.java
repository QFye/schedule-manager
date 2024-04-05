package com.example.service;

import com.example.entity.Event;
import com.example.entity.Schedule;
import com.example.mapper.EventMapper;
import com.example.mapper.ScheduleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class EventService {

    @Resource
    private EventMapper eventMapper;
    @Resource
    private ScheduleMapper scheduleMapper;

    /**
     * 新增
     */
    public void add(Event event) {
        eventMapper.insert(event);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        eventMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            eventMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Event event) {
        eventMapper.updateById(event);
    }

    /**
     * 根据ID查询
     */
    public Event selectById(Integer id) {
        return eventMapper.selectById(id);
    }


    public List<Event> selectByUserAndDate(Integer id, Date date) {
        return eventMapper.selectByUserAndDate(id, date);
    }

    /**
     * 查询所有
     */
    public List<Event> selectAll(Event event) {
        return eventMapper.selectAll(event);
    }

    /**
     * 分页查询
     */
    public PageInfo<Event> selectPage(Event event, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventMapper.selectAll(event);
        return PageInfo.of(list);
    }

    public List<Event> selectTop10() {
        return eventMapper.selectTop10();
    }

    public List<Event> selectByCategoryId(Integer id) {
        return eventMapper.selectByCategoryId(id);
    }

    public List<Event> selectByBusinessId(Integer id) {return eventMapper.selectByBusinessId(id);}

    public List<Event> selectPersonal(Integer id) {return eventMapper.selectPersonal(id);}

    public void addRepository(Event event, Integer userId) {
        eventMapper.updateById(event);
        eventMapper.addRepository(event, userId);}

    public void addInSchedule(Event event, Date date, Integer userId) {
        eventMapper.insert(event);
        Schedule schedule = scheduleMapper.selectByIdAndDate(userId, date);
        if(schedule == null){
            schedule = new Schedule(date, userId);
            scheduleMapper.insert(schedule);
        }
        eventMapper.insertIntoSchedule(event, schedule);
    }

    public void deleteInSchedule(Integer id, Date date, Integer userId) {
        Schedule schedule = scheduleMapper.selectByIdAndDate(userId, date);
        eventMapper.deleteInSchedule(id, schedule);
        eventMapper.deleteById(id);
    }

    public Event selectFromRepository(Integer eventId, Integer userId) {
        return eventMapper.selectFromRepository(eventId, userId);
    }
}