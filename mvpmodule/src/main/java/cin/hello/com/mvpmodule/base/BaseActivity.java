package cin.hello.com.mvpmodule.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author
 * Description：
 */
public abstract class BaseActivity extends AppCompatActivity {
    //private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutId());
        //unbinder = ButterKnife.bind(this);
        findViews();
        initView();
    }

    @Override
    protected void onDestroy() {
        //unbinder.unbind();
        super.onDestroy();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 找到对应的控件
     */
    public abstract void findViews();

}
