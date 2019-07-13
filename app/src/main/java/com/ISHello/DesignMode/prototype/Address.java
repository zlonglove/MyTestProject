package com.ISHello.DesignMode.prototype;

public class Address implements Cloneable{
    public String city;
    public String district;
    public String street;

    public Address(String city, String district, String street) {
        this.city = city;
        this.district = district;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    protected Address clone() throws CloneNotSupportedException {
        return (Address)super.clone();
    }
}
