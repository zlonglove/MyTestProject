package com.ISHello.Contentprovider;

public class employeeInfo {
    private String name;
    private int age;
    private String gender;
    private int _id;

    public employeeInfo() {
        super();
    }

    public employeeInfo(String name, int age, String gender, int _id) {
        super();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

}
