package com.example.tomate.ui.model;

public class User {


    private long userId;
    private String userName; //유저이름
    private String tier; //티어 ex) 토마토 마스터
    private long Tomato; //토마토 소지 개수
    private String totalStudyTime;
    private int tierImageID;// 티어 이미지 리소스
    private String startDate;

    public User(long userId, String userName, String tier, long Tomato, String totalStudyTime, int tierImageID, String startDate) {
        setUserId(userId);
        setUserName(userName);
        setTier(tier);
        setTomato(Tomato);
        setTotalStudyTime(totalStudyTime);
        setTierImageID(tierImageID);
        setStartDate(startDate);
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(String totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getTierImageID() {
        return tierImageID;
    }

    public void setTierImageID(int tierImageID) {
        this.tierImageID = tierImageID;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public long getTomato() {
        return Tomato;
    }

    public void setTomato(long Tomato) {
        this.Tomato = Tomato;
    }

    public User(){

    }
}
