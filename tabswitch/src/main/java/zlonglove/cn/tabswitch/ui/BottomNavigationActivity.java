package zlonglove.cn.tabswitch.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import zlonglove.cn.tabswitch.R;
import zlonglove.cn.tabswitch.base.BaseActivity;
import zlonglove.cn.tabswitch.base.BaseFragment;

/**
 * <p> </p>
 *
 * @author 2017/9/27 10:23
 * @version V1.1
 * @name BottomNavigationActivity
 */
public class BottomNavigationActivity extends BaseActivity {

    private NoScrollViewPager mPager;
    private List<BaseFragment> mFragments;
    private FragmentAdapter mAdapter;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.navigation_home) {
                mPager.setCurrentItem(0);
                return true;
            } else if (i == R.id.navigation_dashboard) {
                mPager.setCurrentItem(1);
                return true;
            } else if (i == R.id.navigation_notifications) {
                mPager.setCurrentItem(2);
                return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViewPager();
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();
        /**
         * 主动寻找
         */
        BaseFragment newsFragment = getNewsFragment();

        mFragments.add(newsFragment);
        mFragments.add(getGirlsFragment());
        mFragments.add(getReservedFragment());

        mPager = (NoScrollViewPager) findViewById(R.id.container_pager);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        mPager.setPagerEnabled(true);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_notifications);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setOffscreenPageLimit(0); //预加载剩下两页
    }


    /**
     * 创建NewsFragment
     *
     * @return Fragment
     */
    private BaseFragment getNewsFragment() {
        return NewsFragment.newInstance();
    }

    private BaseFragment getGirlsFragment() {
        return GirlsFragment.newInstance();
    }

    private BaseFragment getReservedFragment() {
        return ReservedFragment.newInstance("1", "2");
    }

}
