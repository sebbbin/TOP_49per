package com.example.tomate.ui.model;

public class RecordData {

    private String Date;
    private String pure; // 순 공부시간
    private String total; /// 총 공부시간

    public String getDate() {
        return Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getPure() {
        return pure;
    }
    public void setPure(String pure) {
        this.pure = pure;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }

}
