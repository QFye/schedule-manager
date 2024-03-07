package com.example.service;
import com.example.entity.TemplateCategory;
import com.example.mapper.TemplateCategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日程事件分类信息表业务处理
 **/
@Service
public class TemplateCategoryService {

    @Resource
    private TemplateCategoryMapper templateCategoryMapper;

    /**
     * 新增
     */
    public void add(TemplateCategory templateCategory) {
        templateCategoryMapper.insert(templateCategory);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        templateCategoryMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            templateCategoryMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(TemplateCategory templateCategory) {
        templateCategoryMapper.updateById(templateCategory);
    }

    /**
     * 根据ID查询
     */
    public TemplateCategory selectById(Integer id) {
        return templateCategoryMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<TemplateCategory> selectAll(TemplateCategory templateCategory) {
        return templateCategoryMapper.selectAll(templateCategory);
    }

    /**
     * 分页查询
     */
    public PageInfo<TemplateCategory> selectPage(TemplateCategory templateCategory, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TemplateCategory> list = templateCategoryMapper.selectAll(templateCategory);
        return PageInfo.of(list);
    }

}