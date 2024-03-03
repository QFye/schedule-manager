package com.example.service;

import com.example.entity.Schedule;
import com.example.mapper.ScheduleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    /**
     * 新增
     */
    public void add(Schedule schedule) {
        scheduleMapper.insert(schedule);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        scheduleMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scheduleMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Schedule schedule) {
        scheduleMapper.updateById(schedule);
    }

    /**
     * 根据ID查询
     */
    public Schedule selectById(Integer id) {
        return scheduleMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Schedule> selectAll(Schedule schedule) {
        return scheduleMapper.selectAll(schedule);
    }

    /**
     * 分页查询
     */
    public PageInfo<Schedule> selectPage(Schedule schedule, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Schedule> list = scheduleMapper.selectAll(schedule);
        return PageInfo.of(list);
    }

}