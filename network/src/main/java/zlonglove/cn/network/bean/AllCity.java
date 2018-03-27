package zlonglove.cn.network.bean;

import com.in.zlonglove.commonutil.minify.Gsonable;

import java.util.ArrayList;

/**
 * Created by zhanglong on 2018/3/21.
 */

public class AllCity extends Gsonable{
    private String error_code;
    private String reason;
    private String resultcode;
    private ArrayList<City> result;

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public void setResult(ArrayList<City> result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public String getReason() {
        return reason;
    }

    public String getResultcode() {
        return resultcode;
    }

    public ArrayList<City> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "AllCity{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", resultcode='" + resultcode + '\'' +
                ", result=" + result +
                '}';
    }
}
