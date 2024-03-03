package com.example.service;
import com.example.entity.ScheduleCategory;
import com.example.mapper.ScheduleCategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日程事件分类信息表业务处理
 **/
@Service
public class ScheduleCategoryService {

    @Resource
    private ScheduleCategoryMapper scheduleCategoryMapper;

    /**
     * 新增
     */
    public void add(ScheduleCategory scheduleCategory) {
        scheduleCategoryMapper.insert(scheduleCategory);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        scheduleCategoryMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scheduleCategoryMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(ScheduleCategory scheduleCategory) {
        scheduleCategoryMapper.updateById(scheduleCategory);
    }

    /**
     * 根据ID查询
     */
    public ScheduleCategory selectById(Integer id) {
        return scheduleCategoryMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<ScheduleCategory> selectAll(ScheduleCategory scheduleCategory) {
        return scheduleCategoryMapper.selectAll(scheduleCategory);
    }

    /**
     * 分页查询
     */
    public PageInfo<ScheduleCategory> selectPage(ScheduleCategory scheduleCategory, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ScheduleCategory> list = scheduleCategoryMapper.selectAll(scheduleCategory);
        return PageInfo.of(list);
    }

}