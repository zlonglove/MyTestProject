package com.ISHello.DAO;

import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import com.ISHello.Application.BaseApplication;
import com.ISHello.Area.SystemArea;
import com.example.ishelloword.R;

import java.util.ArrayList;


public class SystemAreaDao {

    private static SystemAreaDao sytemAreaDao = null;

    private SystemAreaDao() {
    }

    public synchronized static SystemAreaDao getInstance() {
        if (sytemAreaDao == null) {
            synchronized (SystemArea.class) {
                if (sytemAreaDao == null) {
                    sytemAreaDao = new SystemAreaDao();
                }
            }
        }
        return sytemAreaDao;
    }

    /**
     * 方法简介：查询地区表所有的省份名
     * <p>输入项说明：无
     * <p>返回项说明：省份名称的TreeSet集合
     */


    /**
     * 方法简介：查询地区表所有的省份名
     * <p>输入项说明：无
     * <p>返回项说明：省份名称的TreeSet集合
     */
    public ArrayList<String> querySystemProvinceList() {
        ArrayList<String> provinceSet = new ArrayList<>();
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        if (!provinceSet.contains(xrp.getAttributeValue(1))) {
                            provinceSet.add(xrp.getAttributeValue(1));
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return provinceSet;
    }


    /**
     * 方法简介：根据省份名查询它下面的所有城市
     * <p>输入项说明：省份名称
     * <p>返回项说明：该省下面对应的城市集合
     */
    public ArrayList<String> querySystemCityList(String provinceName) {
        ArrayList<String> cityList = new ArrayList<>();
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        if (xrp.getAttributeValue(1).equals(provinceName)) {
                            if ((!TextUtils.isEmpty(xrp.getAttributeValue(2))) && (!xrp.getAttributeValue(2).endsWith("NULL"))) {
                                cityList.add(xrp.getAttributeValue(2));
                            }
                        }
                    }
                }
                xrp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cityList;
    }

    /**
     * 方法简介：查询序列号
     * <p>输入项说明：省份名称，城市名称
     * <p>返回项说明：该省或市对应的序列号
     */
    public int querySystemSerial(String provinceName, String cityName) {
        int serialNum = -1;
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        /*
                         * 省市  都是空 返回空
						 * 省     不是空  市为空      返回省的代码 查不到返回空
						 * 省     为空      市不为空  返回市的代码 查不到返回空
						 * 省     不为空  市不为空  如果相同则以省计算 查不到返回空
						 * */
                        if (TextUtils.isEmpty(provinceName) && TextUtils.isEmpty(cityName)) {
                            break;
                        } else if ((!TextUtils.isEmpty(provinceName)) && TextUtils.isEmpty(cityName)) {
                            if (xrp.getAttributeValue(1).equals(provinceName)) {
                                serialNum = Integer.parseInt(xrp.getAttributeValue(0));
                                break;
                            }
                        } else if ((TextUtils.isEmpty(provinceName)) && (!TextUtils.isEmpty(cityName))) {
                            if (xrp.getAttributeValue(1).equals(cityName)) {
                                serialNum = Integer.parseInt(xrp.getAttributeValue(0));
                                break;
                            } else if (xrp.getAttributeValue(2).equals(cityName)) {
                                serialNum = Integer.parseInt(xrp.getAttributeValue(0));
                                break;
                            }
                        } else if ((!TextUtils.isEmpty(provinceName)) && (!TextUtils.isEmpty(cityName))) {
                            if (provinceName.equals(cityName)) {
                                if (xrp.getAttributeValue(1).equals(provinceName)) {
                                    serialNum = Integer.parseInt(xrp.getAttributeValue(0));
                                    break;
                                }
                            } else {
                                if (xrp.getAttributeValue(2).equals(cityName)) {
                                    serialNum = Integer.parseInt(xrp.getAttributeValue(0));
                                    break;
                                }
                            }
                        }

                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return serialNum;
    }


    /**
     * 方法简介：根据序列号查询对应的地区名
     * <p>输入项说明：序列号
     * <p>返回项说明：序列号对应的地区名
     */
    public String querySystemAreaNameBySerial(int seriNo) {
        String provinceName = "";
        String cityName = "";
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        /*
                         * 省市  都是空 返回空
						 * 省     不是空  市为空      返回省的代码 查不到返回空
						 * 省     为空      市不为空  返回市的代码 查不到返回空
						 * 省     不为空  市不为空  如果相同则以省计算 查不到返回空
						 * */
                        if (Integer.parseInt(xrp.getAttributeValue(0)) == seriNo) {
                            provinceName = xrp.getAttributeValue(1);
                            cityName = xrp.getAttributeValue(2);
                            if ("NULL".equals(provinceName)) {
                                provinceName = "";
                            }
                            if ("NULL".equals(cityName)) {
                                cityName = "";
                            }
                            break;
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return provinceName + " " + cityName;
    }


    /*获取省份代码*/
    private String queryICBCProvinceAreaCodeByAreaId(int seriNo) {
        String mResult = "";
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        if (Integer.parseInt(xrp.getAttributeValue(0)) == seriNo) {
                            mResult = xrp.getAttributeValue(7);
                            break;
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mResult;
    }


    /*获取省的名称 */
    private String queryICBCProvinceNameByAreaId(int seriNo) {
        String mICBCAreaCode = queryICBCProvinceAreaCodeByAreaId(seriNo);
        String mResult = "";
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        if ((!TextUtils.isEmpty(xrp.getAttributeValue(6))) && xrp.getAttributeValue(6).equals(mICBCAreaCode)) {
                            mResult = xrp.getAttributeValue(8);
                            if ("NULL".equals(mResult)) {
                                mResult = "";
                            }
                            break;
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResult;
    }


    /*获取市的名称 */
    public String queryICBCCityNameByAreaId(int seriNo) {
        String mResult = "";
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        if (Integer.parseInt(xrp.getAttributeValue(0)) == seriNo && (!TextUtils.isEmpty(xrp.getAttributeValue(6)))) {
                            mResult = xrp.getAttributeValue(8);
                            if ("NULL".equals(mResult)) {
                                mResult = "";
                            }
                            break;
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResult;
    }

    /*获取行内省市名称 */
    private String queryICBCAreaNameByAreaId(int seriNo) {
        String provinceName = "";
        String cityName = "";
        try {
            /*
             * <ITEM
			 * AREACODE="1"  
			 * PROVINCE_NAME="北京市"  
			 * CITY_NAME="NULL"  
			 * AREA_LEVEL="1" 
			 * VERSION_NO="3" 
			 * UPDATETIME="10/08/2014 14:55:38" 
			 * ICBCAREACODE="00200" 
			 * ICBCPROCODE="00200" 
			 * AREANAME="北京" >
			 * */
            provinceName = queryICBCProvinceNameByAreaId(seriNo);
            cityName = queryICBCCityNameByAreaId(seriNo);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return provinceName + " " + cityName;
    }


    /*获取行内省市名称 */
    public String queryICBCAreaNameByAreaName(String provinceName, String cityName) {
        String mProvinceName = "";
        String mCityName = "";
        try {
            /*
             * <ITEM
			 * AREACODE="1"  
			 * PROVINCE_NAME="北京市"  
			 * CITY_NAME="NULL"  
			 * AREA_LEVEL="1" 
			 * VERSION_NO="3" 
			 * UPDATETIME="10/08/2014 14:55:38" 
			 * ICBCAREACODE="00200" 
			 * ICBCPROCODE="00200" 
			 * AREANAME="北京" >
			 * */
            int seriNo = querySystemSerial(provinceName, cityName);
            mProvinceName = queryICBCProvinceNameByAreaId(seriNo);
            mCityName = queryICBCCityNameByAreaId(seriNo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mProvinceName + " " + mCityName;
    }


    /**
     * 方法简介：根据序列号查询对应的地区名
     * <p>输入项说明：序列号
     * <p>返回项说明：序列号对应的地区名
     */
    public String queryICBCAreaCodeBySerialNo(int seriNo) {
        String ICBCAreaCode = "";
        XmlResourceParser xrp = BaseApplication.getInstance().getApplicationContext().getResources().getXml(R.xml.area);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("ITEM")) {
                        /*
                         * 省市  都是空 返回空
						 * 省     不是空  市为空      返回省的代码 查不到返回空
						 * 省     为空      市不为空  返回市的代码 查不到返回空
						 * 省     不为空  市不为空  如果相同则以省计算 查不到返回空
						 * */
                        if (Integer.parseInt(xrp.getAttributeValue(0)) == seriNo) {
                            if ((!TextUtils.isEmpty(xrp.getAttributeValue(6))) && (!xrp.getAttributeValue(6).equals("NULL"))) {
                                ICBCAreaCode = xrp.getAttributeValue(6);
                                break;
                            }
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ICBCAreaCode;
    }

}
