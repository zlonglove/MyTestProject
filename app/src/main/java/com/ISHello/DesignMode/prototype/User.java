package com.ISHello.DesignMode.prototype;

public class User implements Cloneable {
    public int age;
    public String phoneNum;
    public String name;
    public Address address;

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address.toString() +
                '}';
    }

    @Override
    protected User clone() {
        User user = null;
        try {
            user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }
}
