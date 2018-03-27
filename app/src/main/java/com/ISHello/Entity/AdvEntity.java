package com.ISHello.Entity;

/**
 * Created by kfzx-zhanglong on 2016/11/2.
 * Company ICBC
 */
public class AdvEntity {
    private String ID;
    private String ADV_ID;
    private String ADV_KIND;
    private String ADV_DESCRIPTION;
    private String ADV_SHOW;
    private String ADV_BEGINDATE;
    private String ADV_ENDDATE;
    private String ADV_PICPATH;
    private String ADV_LOCALPICPATH;
    private String ADV_AREACODE;
    private String ADV_URLTYPE;
    private String ADV_URL;

    public AdvEntity() {
        super();
    }

    public AdvEntity(String ID, String ADV_ID, String ADV_KIND, String ADV_DESCRIPTION, String ADV_SHOW, String ADV_PICPATH, String ADV_LOCALPICPATH, String ADV_AREACODE, String ADV_URLTYPE, String ADV_URL, String ADV_BEGINDATE, String ADV_ENDDATE) {
        super();
        this.ID = ID;
        this.ADV_ID = ADV_ID;
        this.ADV_KIND = ADV_KIND;
        this.ADV_DESCRIPTION = ADV_DESCRIPTION;
        this.ADV_SHOW = ADV_SHOW;
        this.ADV_BEGINDATE = ADV_BEGINDATE;
        this.ADV_ENDDATE = ADV_ENDDATE;
        this.ADV_PICPATH = ADV_PICPATH;
        this.ADV_LOCALPICPATH = ADV_LOCALPICPATH;
        this.ADV_AREACODE = ADV_AREACODE;
        this.ADV_URLTYPE = ADV_URLTYPE;
        this.ADV_URL = ADV_URL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getADV_ID() {
        return ADV_ID;
    }

    public void setADV_ID(String aDV_ID) {
        ADV_ID = aDV_ID;
    }

    public String getADV_KIND() {
        return ADV_KIND;
    }

    public void setADV_KIND(String aDV_KIND) {
        ADV_KIND = aDV_KIND;
    }

    public String getADV_DESCRIPTION() {
        return ADV_DESCRIPTION;
    }

    public void setADV_DESCRIPTION(String aDV_DESCRIPTION) {
        ADV_DESCRIPTION = aDV_DESCRIPTION;
    }

    public String getADV_PICPATH() {
        return ADV_PICPATH;
    }

    public void setADV_PICPATH(String aDV_PICPATH) {
        ADV_PICPATH = aDV_PICPATH;
    }

    public String getADV_LOCALPICPATH() {
        return ADV_LOCALPICPATH;
    }

    public void setADV_LOCALPICPATH(String aDV_LOCALPICPATH) {
        ADV_LOCALPICPATH = aDV_LOCALPICPATH;
    }

    public String getADV_AREACODE() {
        return ADV_AREACODE;
    }

    public void setADV_AREACODE(String aDV_AREACODE) {
        ADV_AREACODE = aDV_AREACODE;
    }

    public String getADV_URLTYPE() {
        return ADV_URLTYPE;
    }

    public void setADV_URLTYPE(String aDV_URLTYPE) {
        ADV_URLTYPE = aDV_URLTYPE;
    }

    public String getADV_URL() {
        return ADV_URL;
    }

    public void setADV_URL(String aDV_URL) {
        ADV_URL = aDV_URL;
    }

    public String getADV_SHOW() {
        return ADV_SHOW;
    }

    public void setADV_SHOW(String aDV_SHOW) {
        ADV_SHOW = aDV_SHOW;
    }

    public String getADV_BEGINDATE() {
        return ADV_BEGINDATE;
    }

    public void setADV_BEGINDATE(String aDV_BEGINDATE) {
        ADV_BEGINDATE = aDV_BEGINDATE;
    }

    public String getADV_ENDDATE() {
        return ADV_ENDDATE;
    }

    public void setADV_ENDDATE(String aDV_ENDDATE) {
        ADV_ENDDATE = aDV_ENDDATE;
    }
}
