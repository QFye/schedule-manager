package com.example.controller;

import com.example.common.Result;
import com.example.entity.TemplateCategory;
import com.example.service.TemplateCategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/templateCategory")
public class TemplateCategoryController {

    @Resource
    private TemplateCategoryService templateCategoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody TemplateCategory templateCategory) {
        templateCategoryService.add(templateCategory);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        templateCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        templateCategoryService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody TemplateCategory templateCategory) {
        templateCategoryService.updateById(templateCategory);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        TemplateCategory templateCategory = templateCategoryService.selectById(id);
        return Result.success(templateCategory);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(TemplateCategory templateCategory ) {
        List<TemplateCategory> list = templateCategoryService.selectAll(templateCategory);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(TemplateCategory templateCategory,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TemplateCategory> page = templateCategoryService.selectPage(templateCategory, pageNum, pageSize);
        return Result.success(page);
    }

}