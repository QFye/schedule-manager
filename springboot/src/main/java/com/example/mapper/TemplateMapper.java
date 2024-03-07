package com.example.mapper;

import com.example.entity.Template;

import java.util.List;

/**
 * 操作template相关数据接口
*/
public interface TemplateMapper {

    /**
      * 新增
    */
    int insert(Template template);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Template template);

    /**
      * 根据ID查询
    */
    Template selectById(Integer id);

    /**
      * 查询所有
    */
    List<Template> selectAll(Template template);

}