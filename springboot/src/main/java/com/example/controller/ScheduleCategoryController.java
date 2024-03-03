package com.example.controller;

import com.example.common.Result;
import com.example.entity.ScheduleCategory;
import com.example.service.ScheduleCategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/scheduleCategory")
public class ScheduleCategoryController {

    @Resource
    private ScheduleCategoryService scheduleCategoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ScheduleCategory scheduleCategory) {
        scheduleCategoryService.add(scheduleCategory);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        scheduleCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        scheduleCategoryService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody ScheduleCategory scheduleCategory) {
        scheduleCategoryService.updateById(scheduleCategory);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ScheduleCategory scheduleCategory = scheduleCategoryService.selectById(id);
        return Result.success(scheduleCategory);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ScheduleCategory scheduleCategory ) {
        List<ScheduleCategory> list = scheduleCategoryService.selectAll(scheduleCategory);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ScheduleCategory scheduleCategory,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ScheduleCategory> page = scheduleCategoryService.selectPage(scheduleCategory, pageNum, pageSize);
        return Result.success(page);
    }

}