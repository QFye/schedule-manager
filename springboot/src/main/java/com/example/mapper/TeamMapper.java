package com.example.mapper;

import com.example.entity.Team;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作team相关数据接口
*/
public interface TeamMapper {

    /**
      * 新增
    */
    int insert(Team team);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Team team);

    /**
      * 根据ID查询
    */
    Team selectById(Integer id);

    /**
      * 查询所有
    */
    List<Team> selectAll(Team team);

    @Select("select * from team where name = #{name}")
    Team selectByName(String name);

    @Select("select *, user.name as user from team,user,teammemberrelationship where teammemberrelationship.userId = user.id and teammemberrelationship.teamId = team.id and user.id=#{id}")
    List<Team> selectByUserId(Integer id);

    @Select("select *, M.memberCount as memberCount from team left join user on team.userId = user.id left join (select team.id as tid, COUNT(user.id) as memberCount from team left join TeamMemberRelationship on TeamMemberRelationship.teamId = team.id left join user on TeamMemberRelationship.userId = user.id group by team.id) as M on M.tid = team.id where team.userId = user.id and user.id = #{id}")
    List<Team> selectByManagerId(Integer id);
}