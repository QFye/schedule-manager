package com.example.controller;

import com.example.common.Result;
import com.example.entity.Event;
import com.example.entity.History;
import com.example.service.EventService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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

    @PostMapping("/addInSchedule")
    public Result addInSchedule(@RequestBody Event event, @RequestParam Date date, @RequestParam Integer userId) {
        eventService.addInSchedule(event, date, userId);
        return Result.success();
    }

    @PostMapping("/applyInSchedule")
    public Result applyInSchedule(@RequestBody Event event, @RequestParam Date date, @RequestParam Integer userId, @RequestParam Integer initialId) {
        eventService.applyInSchedule(event, date, userId, initialId);
        return Result.success();
    }

    @PostMapping("/addRepository")
    public Result addRepository(@RequestBody Event event, @RequestParam Integer userId) {
        eventService.addRepository(event, userId);
        return Result.success();
    }

    @PostMapping("/addInRepository")
    public Result addInRepository(@RequestBody Event event, @RequestParam Integer userId) {
        eventService.addInRepository(event, userId);
        return Result.success();
    }

    @PostMapping("/addRepositoryOnly")
    public Result addRepositoryOnly(@RequestBody Event event, @RequestParam Integer userId) {
        eventService.addRepositoryOnly(event, userId);
        return Result.success();
    }

    @PostMapping("/sendEvent/{teamId}")
    public Result sendEvent(@RequestBody Event event, @PathVariable Integer teamId, @RequestParam Integer currentUserId) {
        eventService.sendEvent(event, teamId, currentUserId);
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


    @DeleteMapping("/deleteInSchedule/{id}")
    public Result deleteInSchedule(@PathVariable Integer id,@RequestParam Date date,@RequestParam Integer userId) {
        eventService.deleteInSchedule(id, date, userId);
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

    @GetMapping("/selectFromRepository")
    public Result selectFromRepository(@RequestParam Integer eventId, @RequestParam Integer userId) {
        Event event = eventService.selectFromRepository(eventId, userId);
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

    @GetMapping("/generateSchedule/{userId}")
    public Result generateSchedule(@PathVariable Integer userId, @RequestParam Date date) {
        List<Event> list = eventService.generateSchedule(userId, date);
        return Result.success(list);
    }

    @GetMapping("/getHistory")
    public Result getHistory() {
        List<History> list = eventService.getHistory();
        return Result.success(list);
    }

    @GetMapping("/selectAllFromRepository/{repositoryUserId}")
    public Result selectAllFromRepository(Event event, @PathVariable Integer repositoryUserId) {
        List<Event> list = eventService.selectAllFromRepository(event, repositoryUserId);
        return Result.success(list);
    }

    @GetMapping("/selectTop10")
    public Result selectTop10() {
        List<Event> list = eventService.selectTop10();
        return Result.success(list);
    }

    @GetMapping("/recommend")
    public Result recommend() {
        List<Event> list = eventService.recommend(null, 10);
        return Result.success(list);
    }

    @GetMapping("/recommendByCategory/{categoryId}")
    public Result recommend(@PathVariable Integer categoryId) {
        List<Event> list = eventService.recommend(categoryId, 5);
        return Result.success(list);
    }

    @GetMapping("/selectByCategoryId")
    public Result selectByCategoryId(@RequestParam Integer id) {
        List<Event> list = eventService.selectByCategoryId(id);
        return Result.success(list);
    }

    @GetMapping("/selectByBusinessId")
    public Result selectByBusinessId(@RequestParam Integer id) {
        List<Event> list = eventService.selectByBusinessId(id);
        return Result.success(list);
    }

    @GetMapping("/selectByUserAndDate")
    public Result selectByUserAndDate(@RequestParam Integer id, @RequestParam Date date) {
        List<Event> list = eventService.selectByUserAndDate(id, date);
        return Result.success(list);
    }

    @GetMapping("/selectPersonal")
    public Result selectPersonal(@RequestParam Integer id) {
        List<Event> event = eventService.selectPersonal(id);
        return Result.success(event);
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

    @GetMapping("/selectRepositoryPage/{repositoryUserId}")
    public Result selectRepositoryPage(Event event, @PathVariable Integer repositoryUserId,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Event> page = eventService.selectRepositoryPage(event, repositoryUserId, pageNum, pageSize);
        return Result.success(page);
    }

}