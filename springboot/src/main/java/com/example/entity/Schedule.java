package com.example.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公告信息表
*/
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 计划日期 */
    private Date date;
    /** 用户编号 */
    private Integer userId;

    private String user;

    private Integer eventCount;

    public Schedule() {
    }

    public Schedule(Date date, Integer userId) {
        this.date = date;
        this.userId = userId;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}