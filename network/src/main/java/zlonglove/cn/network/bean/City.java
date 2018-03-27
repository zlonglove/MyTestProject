package zlonglove.cn.network.bean;

import com.in.zlonglove.commonutil.minify.Gsonable;

/**
 * Created by zhanglong on 2018/3/21.
 */

public class City extends Gsonable {
    /**
     * id : 1
     * province : 北京
     * city : 北京
     * district : 北京
     */

    private String id;
    private String province;
    private String city;
    private String district;

    public void setId(String id) {
        this.id = id;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
