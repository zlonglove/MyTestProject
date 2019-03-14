package zlonglove.cn.base.test.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.bar.TitleBar;

/**
 * @author : zl
 * @github :
 * @time : 2018/10/18
 * @desc : 项目中 Fragment 懒加载基类
 */
public abstract class MyLazyFragment extends UILazyFragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    public TitleBar getTitleBar() {
        if (getTitleBarId() > 0 && findViewById(getTitleBarId()) instanceof TitleBar) {
            return findViewById(getTitleBarId());
        }
        return null;
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence s) {
       // ToastUtils.show(s);
    }

    public void toast(int id) {
        //ToastUtils.show(id);
    }

    public void toast(Object object) {
        //ToastUtils.show(object);
    }
}