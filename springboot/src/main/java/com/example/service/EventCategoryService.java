package com.example.service;
import com.example.entity.EventCategory;
import com.example.mapper.EventCategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日程事件分类信息表业务处理
 **/
@Service
public class EventCategoryService {

    @Resource
    private EventCategoryMapper eventCategoryMapper;

    /**
     * 新增
     */
    public void add(EventCategory eventCategory) {
        eventCategoryMapper.insert(eventCategory);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        eventCategoryMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            eventCategoryMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(EventCategory eventCategory) {
        eventCategoryMapper.updateById(eventCategory);
    }

    /**
     * 根据ID查询
     */
    public EventCategory selectById(Integer id) {
        return eventCategoryMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<EventCategory> selectAll(EventCategory eventCategory) {
        return eventCategoryMapper.selectAll(eventCategory);
    }

    /**
     * 分页查询
     */
    public PageInfo<EventCategory> selectPage(EventCategory eventCategory, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EventCategory> list = eventCategoryMapper.selectAll(eventCategory);
        return PageInfo.of(list);
    }

}