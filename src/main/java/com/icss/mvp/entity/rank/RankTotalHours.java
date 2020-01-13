package com.icss.mvp.entity.rank;/**
 * Created by chengchenhui on 2019/7/27.
 */

/**
 * @author chengchenhui
 * @title: RankTotalHours
 * @projectName mvp
 * @description: TODO
 * @date 2019/7/2721:08
 */
public class RankTotalHours {
    private String no;
    private String zrAccount;
    private double attendence;//累计出勤工时
    private double overTime;//累计加班工时
    private double salary;//职级薪资


    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getZrAccount() {
        return zrAccount;
    }

    public void setZrAccount(String zrAccount) {
        this.zrAccount = zrAccount;
    }

    public double getAttendence() {
        return attendence;
    }

    public void setAttendence(double attendence) {
        this.attendence = attendence;
    }

    public double getOverTime() {
        return overTime;
    }

    public void setOverTime(double overTime) {
        this.overTime = overTime;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
