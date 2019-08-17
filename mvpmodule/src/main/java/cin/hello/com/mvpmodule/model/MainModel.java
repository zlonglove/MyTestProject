package cin.hello.com.mvpmodule.model;


import cin.hello.com.mvpmodule.bean.BaseObjectBean;
import cin.hello.com.mvpmodule.bean.LoginBean;
import cin.hello.com.mvpmodule.contract.MainContract;
import cin.hello.com.mvpmodule.net.RetrofitClient;
import io.reactivex.Flowable;

/**
 * @author azheng
 * @date 2018/6/4.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public class MainModel  implements MainContract.Model {
    @Override
    public Flowable<BaseObjectBean<LoginBean>> login(String username, String password) {
        return RetrofitClient.getInstance().getApi().login(username,password);
    }
}
