package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Team;
import com.example.mapper.TeamMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class TeamService {

    @Resource
    private TeamMapper teamMapper;

    /**
     * 新增
     */
    public void add(Team team) {
        teamMapper.insert(team);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        teamMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            teamMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Team team) {
        teamMapper.updateById(team);
    }

    /**
     * 根据ID查询
     */
    public Team selectById(Integer id) {
        return teamMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Team> selectAll(Team team) {
        return teamMapper.selectAll(team);
    }

    /**
     * 分页查询
     */
    public PageInfo<Team> selectPage(Team team, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Team> list = teamMapper.selectAll(team);
        return PageInfo.of(list);
    }

    public List<Team> selectByUserId(Integer id) {return teamMapper.selectByUserId(id);}

    public List<Team> selectByManagerId(Integer id) {return teamMapper.selectByManagerId(id);}
}