package com.ISHello.GsonModule;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kfzx-zhanglong on 2016/1/22.
 * Company ICBC
 */
public class ProductsList {

    /**
     * gender : man
     * name : 王五
     * addr : {"code":"300000","province":"fujian","city":"quanzhou"}
     * age : 15
     * height : 140cm
     * hobby : [{"code":"1","name":"billiards"},{"code":"2","name":"computerGame"}]
     */
    @SerializedName("gender")
    private String gender;
    @SerializedName("name")
    private String name;
    @SerializedName("addr")
    private AddrEntity addr;
    @SerializedName("age")
    private int age;
    @SerializedName("height")
    private String height;
    @SerializedName("hobby")
    private List<HobbyEntity> hobby;

    public static ProductsList objectFromData(String str) {

        return new Gson().fromJson(str, ProductsList.class);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddr(AddrEntity addr) {
        this.addr = addr;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setHobby(List<HobbyEntity> hobby) {
        this.hobby = hobby;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public AddrEntity getAddr() {
        return addr;
    }

    public int getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public List<HobbyEntity> getHobby() {
        return hobby;
    }

    public static class AddrEntity {
        /**
         * code : 300000
         * province : fujian
         * city : quanzhou
         */
        @SerializedName("code")
        private String code;
        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;

        public static AddrEntity objectFromData(String str) {

            return new Gson().fromJson(str, AddrEntity.class);
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCode() {
            return code;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        @Override
        public String toString() {
            return "AddrEntity{" +
                    "code='" + code + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    public static class HobbyEntity {
        /**
         * code : 1
         * name : billiards
         */
        @SerializedName("code")
        private String code;
        @SerializedName("name")
        private String name;

        /*public static HobbyEntity objectFromData(String str) {
            return new Gson().fromJson(str, HobbyEntity.class);
        }*/

        public static List<HobbyEntity> arrayHobbyEntityFromData(String str) {
            Type listType = new TypeToken<ArrayList<HobbyEntity>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "HobbyEntity{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
