package com.ISHello.Module;

import com.google.gson.Gson;
import com.in.zlonglove.commonutil.minify.Gsonable;

import java.util.List;

/**
 * Created by zhanglong on 2017/4/6.
 */

/**
 * {
 * "name": "中国",
 * "province": [{
 * "name": "黑龙江",
 * "cities": {
 * "city": ["哈尔滨", "大庆"]
 * }
 * }, {
 * "name": "广东",
 * "cities": {
 * "city": ["广州", "深圳", "珠海"]
 * }
 * }, {
 * "name": "台湾",
 * "cities": {
 * "city": ["台北", "高雄"]
 * }
 * }, {
 * "name": "新疆",
 * "cities": {
 * "city": ["乌鲁木齐"]
 * }
 * }]
 * }
 */

public class CityModule extends Gsonable{
    /**
     * name : 中国
     * province : [{"name":"黑龙江","cities":{"city":["哈尔滨","大庆"]}},{"name":"广东","cities":{"city":["广州","深圳","珠海"]}},{"name":"台湾","cities":{"city":["台北","高雄"]}},{"name":"新疆","cities":{"city":["乌鲁木齐"]}}]
     */

    private String name;
    private List<ProvinceBean> province;

    public static CityModule objectFromData(String str) {
        return new Gson().fromJson(str, CityModule.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProvinceBean> getProvince() {
        return province;
    }

    public void setProvince(List<ProvinceBean> province) {
        this.province = province;
    }

    public static class ProvinceBean {
        /**
         * name : 黑龙江
         * cities : {"city":["哈尔滨","大庆"]}
         */

        private String name;
        private CitiesBean cities;

        public static ProvinceBean objectFromData(String str) {

            return new Gson().fromJson(str, ProvinceBean.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CitiesBean getCities() {
            return cities;
        }

        public void setCities(CitiesBean cities) {
            this.cities = cities;
        }

        public static class CitiesBean {
            private List<String> city;

            public static CitiesBean objectFromData(String str) {

                return new Gson().fromJson(str, CitiesBean.class);
            }

            public List<String> getCity() {
                return city;
            }

            public void setCity(List<String> city) {
                this.city = city;
            }
        }
    }
}
