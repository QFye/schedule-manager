package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作comment相关数据接口
*/
public interface CommentMapper {

    /**
      * 新增
    */
    int insert(Comment comment);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Comment comment);

    /**
      * 根据ID查询
    */
    Comment selectById(Integer id);

    /**
      * 查询所有
    */
    List<Comment> selectAll(Comment comment);

    @Select("select * from comment where name = #{name}")
    Comment selectByName(String name);

    @Select("select comment.time as time, comment.commentContent as commentContent, user.name as name, user.avatar as avatar from comment,user,event where comment.userId = user.id and comment.eventId = event.id and event.id = #{id}")
    List<Comment> selectByEventId(Integer id);
}