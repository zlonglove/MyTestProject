package com.ISHello.GsonModule;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kfzx-zhanglong on 2016/1/22.
 * Company ICBC
 */
public class UserInfo {

    /**
     * gender : man
     * name : 王五
     * age : 15
     * height : 140cm
     */
    @SerializedName("gender")
    private String gender;
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;
    @SerializedName("height")
    private String height;

    public static UserInfo objectFromData(String str) {
        return new Gson().fromJson(str, UserInfo.class);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }
}
