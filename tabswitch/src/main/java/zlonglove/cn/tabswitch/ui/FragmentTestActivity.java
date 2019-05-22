package zlonglove.cn.tabswitch.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import zlonglove.cn.tabswitch.R;
import zlonglove.cn.tabswitch.base.BaseActivity;

public class FragmentTestActivity extends BaseActivity {
    private static final String TAG_ACT = "ACT";
    private static final String TAG_SAVE = "SAVE";

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private LinearLayout ll_act;
    private LinearLayout ll_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        findViews();
        init();
    }

    private void findViews() {
        ll_act = findViewById(R.id.ll_act);
        ll_save = findViewById(R.id.ll_save);
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        ll_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(TAG_ACT);
            }
        });

        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(TAG_SAVE);
            }
        });
        setTitle("活动");
        showFragment(TAG_ACT);
    }

    private void showFragment(String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = (Fragment) mFragmentManager.findFragmentByTag(tag);
        if (mCurrentFragment != null && mCurrentFragment.isVisible()) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.hide(mCurrentFragment);
        }
        if (fragment == null) {
            if (tag.equals(TAG_ACT)) {
                fragment = NewsFragment.newInstance();
            } else if (tag.equals(TAG_SAVE)) {
                fragment = GirlsFragment.newInstance();
            }
            transaction.add(R.id.ff, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
        mCurrentFragment = fragment;
        if (TAG_ACT.equals(tag)) {
            setTitle("活动");
        } else if (tag.equals(TAG_SAVE)) {
            setTitle("收藏");
        }

    }
}
