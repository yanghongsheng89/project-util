package com.yang.event;

import java.io.Serializable;
import java.sql.Timestamp;

public class Event implements Serializable {

    private Timestamp createAt;
    private String createBy;
    private String title;
    private String remark;
    private Timestamp start;
    private Timestamp end;
    private Long id;
    /**
     * NEW:#336699,MOD:#993366,DON:#339966
     */
    private String color;
    /**
     *第一位为删除状态0:删除，1：未删除（默认）
     */
    private Integer controlBit;

    /**
     * 分类ID 1 传统节假日；2：个人记录（默认）
     */
    private Long categoryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getControlBit() {
        return controlBit;
    }

    public void setControlBit(Integer controlBit) {
        this.controlBit = controlBit;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}