package com.example.mapper;

import com.example.entity.TemplateCategory;

import java.util.List;

/**
 * 操作TemplateCategory相关数据接口
*/
public interface TemplateCategoryMapper {

    /**
      * 新增
    */
    int insert(TemplateCategory templateCategory);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(TemplateCategory templateCategory);

    /**
      * 根据ID查询
    */
    TemplateCategory selectById(Integer id);

    /**
      * 查询所有
    */
    List<TemplateCategory> selectAll(TemplateCategory templateCategory);

}