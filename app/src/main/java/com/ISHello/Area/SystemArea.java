package com.ISHello.Area;

/**
 * 此文件涉及嵌入版，请在同步独立版程序时，同步嵌入版程序
 *
 */

/**
 * 移动IM开发项目组 — @author kfzx-ducl 于 2014-9-15 
 * IMAPP_C_ANDROID.com.icbc.im.datastruct.SystemArea.java
 * <p>
 * 这里描述程序简介，由第一位创建人编写：
 * 地区实体类
 * <p>
 * 这里描述历史变更清单，按照变更日期—变更人—变更事项顺序编写。
 * 1、创建基本程序……
 * 
 */
public class SystemArea {
	
	/**
	 * 序列号
	 */
	private int area_id;
	/**
	 * 地区级别
	 */
	private int area_level;
	/**
	 * 版本号
	 */
	private int version_no;
	/**
	 * 省的名字
	 */
	private String province_name;
	/**
	 * 市的名字
	 */
	private String city_name;
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public int getArea_level() {
		return area_level;
	}
	public void setArea_level(int area_level) {
		this.area_level = area_level;
	}
	public int getVersion_no() {
		return version_no;
	}
	public void setVersion_no(int version_no) {
		this.version_no = version_no;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
}
