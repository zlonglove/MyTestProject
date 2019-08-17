package cin.hello.com.mvpmodule.contract;


import cin.hello.com.mvpmodule.base.BaseView;
import cin.hello.com.mvpmodule.bean.BaseObjectBean;
import cin.hello.com.mvpmodule.bean.LoginBean;
import io.reactivex.Flowable;

/**
 * @author azheng
 * @date 2018/6/4.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public interface MainContract {
    interface Model {
        Flowable<BaseObjectBean<LoginBean>> login(String username, String password);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(BaseObjectBean<LoginBean> bean);
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param username
         * @param password
         */
        void login(String username, String password);
    }
}
