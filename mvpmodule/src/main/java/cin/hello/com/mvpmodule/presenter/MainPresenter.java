package cin.hello.com.mvpmodule.presenter;


import cin.hello.com.mvpmodule.base.BasePresenter;
import cin.hello.com.mvpmodule.bean.BaseObjectBean;
import cin.hello.com.mvpmodule.bean.LoginBean;
import cin.hello.com.mvpmodule.contract.MainContract;
import cin.hello.com.mvpmodule.model.MainModel;
import cin.hello.com.mvpmodule.net.RxScheduler;
import io.reactivex.functions.Consumer;

/**
 * @author azheng
 * @date 2018/6/4.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private MainContract.Model model;

    public MainPresenter() {
        model = new MainModel();
    }

    @Override
    public void login(String username, String password) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.login(username, password)
                .compose(RxScheduler.<BaseObjectBean<LoginBean>>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean<LoginBean>>() {
                    @Override
                    public void accept(BaseObjectBean<LoginBean> bean) throws Exception {
                        if (isViewAttached()) {
                            mView.onSuccess(bean);
                            mView.hideLoading();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isViewAttached()) {
                            mView.onError(throwable);
                            mView.hideLoading();
                        }
                    }
                });
    }
}
