package zlonglove.cn.tabswitch.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.in.zlonglove.commonutil.Utils;


/**
 * <p>Fragment的基类</p>
 *
 * @name BaseFragment
 */
@Keep
public abstract class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();
    protected BaseActivity mActivity;

    /**
     * onAttach()回调将在Fragment与其Activity关联之后调用
     * 需要使用Activity的引用或者使用Activity作为其他操作的上下文，将在此回调方法中实现
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
        Log.d(TAG, "--->onAttach()");
    }


    /**
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().addFragment(fragment, frameId);

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().replaceFragment(fragment, frameId);
    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().hideFragment(fragment);
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().showFragment(fragment);
    }


    /**
     * 移除Fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().removeFragment(fragment);

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        getHoldingActivity().popFragment();
    }

}
