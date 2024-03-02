package com.example.controller;

import com.example.common.Result;
import com.example.entity.EventCategory;
import com.example.service.EventCategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/eventCategory")
public class EventCategoryController {

    @Resource
    private EventCategoryService eventCategoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody EventCategory eventCategory) {
        eventCategoryService.add(eventCategory);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        eventCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        eventCategoryService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody EventCategory eventCategory) {
        eventCategoryService.updateById(eventCategory);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        EventCategory eventCategory = eventCategoryService.selectById(id);
        return Result.success(eventCategory);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(EventCategory eventCategory ) {
        List<EventCategory> list = eventCategoryService.selectAll(eventCategory);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(EventCategory eventCategory,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<EventCategory> page = eventCategoryService.selectPage(eventCategory, pageNum, pageSize);
        return Result.success(page);
    }

}