package com.yang.stock;

public class StockNation extends Stock{

    /**
     * create table stockNation(stockId bigInt,nationId bigInt,orderBy int ,hot bool );
     */
    private Long nationId;
    private Long stockId;
    private Integer orderBy;
    private Boolean hot;

    public Long getNationId() {
        return nationId;
    }

    public void setNationId(Long nationId) {
        this.nationId = nationId;
    }
    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }
}
