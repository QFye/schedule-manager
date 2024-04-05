package com.example.controller;

import com.example.common.Result;
import com.example.entity.Team;
import com.example.service.TeamService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/team")
public class TeamController {

    @Resource
    private TeamService teamService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Team team, @RequestParam Integer userId) {
        teamService.add(team, userId);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        teamService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        teamService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Team team) {
        teamService.updateById(team);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Team team = teamService.selectById(id);
        return Result.success(team);
    }

    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Integer id) {
        List<Team> team = teamService.selectByUserId(id);
        return Result.success(team);
    }

    @GetMapping("/selectByManagerId")
    public Result selectByManagerId(@RequestParam Integer id) {
        List<Team> team = teamService.selectByManagerId(id);
        return Result.success(team);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Team team ) {
        List<Team> list = teamService.selectAll(team);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Team team,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Team> page = teamService.selectPage(team, pageNum, pageSize);
        return Result.success(page);
    }

}