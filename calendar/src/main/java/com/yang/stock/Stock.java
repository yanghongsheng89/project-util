package com.yang.stock;

import java.io.Serializable;

public class Stock implements Serializable {

    private Long id;
    private String name;
    private String code;
    private String texturl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTexturl() {
        return texturl;
    }

    public void setTexturl(String texturl) {
        this.texturl = texturl;
    }
}
