package com.example.controller;

import com.example.common.Result;
import com.example.entity.Event;
import com.example.service.EventService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/event")
public class EventController {

    @Resource
    private EventService eventService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Event event) {
        eventService.add(event);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        eventService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        eventService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Event event) {
        eventService.updateById(event);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id) {
        Event event = eventService.selectById(id);
        return Result.success(event);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Event event ) {
        List<Event> list = eventService.selectAll(event);
        return Result.success(list);
    }

    @GetMapping("/selectTop10")
    public Result selectTop10() {
        List<Event> list = eventService.selectTop10();
        return Result.success(list);
    }

    @GetMapping("/selectByCategoryId")
    public Result selectByCategoryId(@RequestParam Integer id) {
        List<Event> list = eventService.selectByCategoryId(id);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Event event,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Event> page = eventService.selectPage(event, pageNum, pageSize);
        return Result.success(page);
    }

}