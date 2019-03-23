package com.yang.stock;


/**
 * 概念 目前60个左右
 */
public class Nation {

    /**
     * create table notion(id bigInt auto_increment primary key ,notionName varchar(64),remark varchar(1024));
     */
    private Long id;
    private String notionName;
    private String remark;

    /**
     * 右边起第一位为是否可用
     */
    private Integer controlBit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getControlBit() {
        return controlBit;
    }

    public void setControlBit(Integer controlBit) {
        this.controlBit = controlBit;
    }

    public String getNotionName() {
        return notionName;
    }

    public void setNotionName(String notionName) {
        this.notionName = notionName;
    }
}
