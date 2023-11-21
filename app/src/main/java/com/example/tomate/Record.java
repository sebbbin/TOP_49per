package com.example.tomate;

import java.time.LocalDate;
import java.util.List;

public class Record {
    private String userId;
    private String date;
    private String total_study_time;
    private String pure_study_time;
    private String tomato_cnt;
    private List<Integer> seconds;

    // 기본 생성자
    public Record() {
    }

    // 매개변수가 있는 생성자
    public Record(String userId, LocalDate now, int total_study_time, int pure_study_time, String tomato_cnt, List<Integer> seconds) {
        this.userId = userId;
        this.date = now.toString();
        this.total_study_time = String.format("%02d:%02d:%02d", total_study_time / 3600, (total_study_time % 3600) / 60, total_study_time % 60);
        this.pure_study_time = String.format("%02d:%02d:%02d", pure_study_time / 3600, (pure_study_time % 3600) / 60, pure_study_time % 60);
        this.tomato_cnt = tomato_cnt;
        this.seconds = seconds;
    }

    // 게터와 세터 메소드
    public String getUserId() {
        return date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_study_time() {
        return total_study_time;
    }

    public void setTotal_study_time(String total_study_time) {
        this.total_study_time = total_study_time;
    }

    public String getPure_study_time() {
        return pure_study_time;
    }

    public void setPure_study_time(String pure_study_time) {
        this.pure_study_time = pure_study_time;
    }

    public String getTomato_cnt() {
        return tomato_cnt;
    }

    public void setTomato_cnt(String tomato_cnt) {
        this.tomato_cnt = tomato_cnt;
    }

    public List<Integer> getSeconds() {
        return seconds;
    }

    public void setSeconds(List<Integer> seconds) {
        this.seconds = seconds;
    }
}
