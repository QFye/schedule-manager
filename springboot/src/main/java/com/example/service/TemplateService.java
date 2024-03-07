package com.example.service;

import com.example.entity.Template;
import com.example.mapper.TemplateMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class TemplateService {

    @Resource
    private TemplateMapper templateMapper;

    /**
     * 新增
     */
    public void add(Template template) {
        templateMapper.insert(template);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        templateMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            templateMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Template template) {
        templateMapper.updateById(template);
    }

    /**
     * 根据ID查询
     */
    public Template selectById(Integer id) {
        return templateMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Template> selectAll(Template template) {
        return templateMapper.selectAll(template);
    }

    /**
     * 分页查询
     */
    public PageInfo<Template> selectPage(Template template, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Template> list = templateMapper.selectAll(template);
        return PageInfo.of(list);
    }

}