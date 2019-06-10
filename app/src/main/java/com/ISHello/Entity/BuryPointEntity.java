package com.ISHello.Entity;

public class BuryPointEntity {
    private final String buryPointId = "buryPointId";
    private final String areano = "areano";
    private final String areanoname = "areanoname";
    private final String buttonno = "buttonno";
    private final String buttonname = "buttonname";

    public String getBuryPointId() {
        return buryPointId;
    }

    public String getAreano() {
        return areano;
    }

    public String getAreanoname() {
        return areanoname;
    }

    public String getButtonno() {
        return buttonno;
    }

    public String getButtonname() {
        return buttonname;
    }

    @Override
    public String toString() {
        return "BuryPointEntity{" +
                "buryPointId='" + buryPointId + '\'' +
                ", areano='" + areano + '\'' +
                ", areanoname='" + areanoname + '\'' +
                ", buttonno='" + buttonno + '\'' +
                ", buttonname='" + buttonname + '\'' +
                '}';
    }
}
