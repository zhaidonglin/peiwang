package com.winm.peiwang.domain;

import android.widget.Button;
import android.widget.Spinner;

import org.kymjs.kjframe.database.annotate.Id;

/**
 * Created by Administrator on 2016/6/26 0026.
 */
public class PointInfo {

    @Id()
    private int id;
//    名称
    private String name;
//    经度
    private double point_x;
//    纬度
    private double point_y;
//    名称选型
    private String names;
//    杆型
    private String pole;
//    高低压同杆
    private String hl_pole;
//    2表位数
    private int two_epi;
//    4表位数
    private int four_epi;
//    8表位数
    private int eight_epi;
//    照明下用户
    private int users;
//    下户线型号
    private String next_moudle;
//   动力下户数
    private int next_users;

    public String getNext_moudle1() {
        return next_moudle1;
    }

    public void setNext_moudle1(String next_moudle1) {
        this.next_moudle1 = next_moudle1;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getNext_moudle() {
        return next_moudle;
    }

    public void setNext_moudle(String next_moudle) {
        this.next_moudle = next_moudle;
    }

    public int getNext_users() {
        return next_users;
    }

    public void setNext_users(int next_users) {
        this.next_users = next_users;
    }

    //    下户线型号
    private  String next_moudle1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoint_x() {
        return point_x;
    }

    public void setPoint_x(double point_x) {
        this.point_x = point_x;
    }

    public double getPoint_y() {
        return point_y;
    }

    public void setPoint_y(double point_y) {
        this.point_y = point_y;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public int getTwo_epi() {
        return two_epi;
    }

    public void setTwo_epi(int two_epi) {
        this.two_epi = two_epi;
    }

    public int getFour_epi() {
        return four_epi;
    }

    public void setFour_epi(int four_epi) {
        this.four_epi = four_epi;
    }

    public int getEight_epi() {
        return eight_epi;
    }

    public void setEight_epi(int eight_epi) {
        this.eight_epi = eight_epi;
    }

    public String getHl_pole() {
        return hl_pole;
    }

    public void setHl_pole(String hl_pole) {
        this.hl_pole = hl_pole;
    }

}
