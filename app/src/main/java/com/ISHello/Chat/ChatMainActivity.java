package com.ISHello.Chat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ISHello.Chat.Listener.OnBaseItemClickListener;
import com.ISHello.Chat.Listener.OnDragDeltaChangeListener;
import com.ISHello.Chat.Listener.OnNetWorkChangedListener;
import com.ISHello.Chat.Receiver.NetWorkChangedReceiver;
import com.ISHello.Chat.Service.GrayService;
import com.ISHello.Chat.View.CustomDatePickerDialog;
import com.ISHello.Chat.View.DragLayout;
import com.ISHello.Chat.View.RoundAngleImageView;
import com.ISHello.Chat.adapter.BaseWrappedViewHolder;
import com.ISHello.Chat.adapter.MenuDisplayAdapter;
import com.ISHello.Constants.Constants;
import com.ISHello.CustomToast.CustomToast;
import com.ISHello.ViewPage.FirstFragment;
import com.ISHello.ViewPage.FourthFragment;
import com.ISHello.ViewPage.SecondFragment;
import com.ISHello.ViewPage.ThirdFragment;
import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;
import com.nineoldandroids.view.ViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class ChatMainActivity extends AppCompatActivity implements OnDragDeltaChangeListener, OnNetWorkChangedListener, View.OnClickListener {
    private Fragment[] mFragments = new Fragment[4];
    private int currentPosition;
    private DragLayout container;
    private RecyclerView menuDisplay;
    private List<String> data = new ArrayList<>();
    private MenuDisplayAdapter menuAdapter;

    private ImageView bg;
    private TextView net;
    private NetWorkChangedReceiver netWorkReceiver;

    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置没有标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat_main);
        initViews();
        initReceiver();
    }

    private void initViews() {
        FirstFragment firstFragment = new FirstFragment();
        SecondFragment secondFragment = new SecondFragment();
        ThirdFragment thirdFragment = new ThirdFragment();
        FourthFragment fourthFragment = new FourthFragment();
        mFragments[0] = firstFragment;
        mFragments[1] = secondFragment;
        mFragments[2] = thirdFragment;
        mFragments[3] = fourthFragment;

        container = (DragLayout) findViewById(R.id.drag_container);
        container.setListener(this);

        menuDisplay = (RecyclerView) findViewById(R.id.rev_menu_display);

        bg = (ImageView) findViewById(R.id.iv_main_bg);
        bg.setAlpha((float) 0.0);
        net = (TextView) findViewById(R.id.tv_main_net);
        net.setOnClickListener(this);

        menuDisplay.setLayoutManager(new LinearLayoutManager(this));
        menuDisplay.setHasFixedSize(true);
        menuDisplay.setItemAnimator(new DefaultItemAnimator());
        initActionBarView();
        initData();
    }

    /**
     * 这里重新构建一个头部布局，因为封装的基类中的头部布局与滑动发生冲突
     */
    private RoundAngleImageView icon_1;
    private TextView right_1;
    private TextView title_1;
    private ImageView rightImage_1;
    protected ImageView back_1;

    private void initActionBarView() {
        icon_1 = (RoundAngleImageView) findViewById(R.id.riv_header_layout_icon);
        right_1 = (TextView) findViewById(R.id.tv_header_layout_right);
        title_1 = (TextView) findViewById(R.id.tv_header_layout_title);
        rightImage_1 = (ImageView) findViewById(R.id.iv_header_layout_right);
        back_1 = (ImageView) findViewById(R.id.iv_header_layout_back);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
    }

    private void initData() {
        addOrReplaceFragment(mFragments[0], R.id.fl_content_container);
        data.add("聊天");
        data.add("好友");
        data.add("邀请");
        data.add("动态");
        menuAdapter = new MenuDisplayAdapter(data, R.layout.menu_item);
        menuDisplay.addOnItemTouchListener(new OnBaseItemClickListener() {
            @Override
            protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                if (currentPosition != position) {
                    addOrReplaceFragment(mFragments[position]);
                    currentPosition = position;
                }
                closeMenu();
            }
        });
        menuDisplay.setAdapter(menuAdapter);
    }

    private void initReceiver() {
        registerReceiver(netWorkReceiver = new NetWorkChangedReceiver(), new IntentFilter(Constants.NETWORK_CONNECTION_CHANGE));
        netWorkReceiver.registerListener(this);
    }

    public void closeMenu() {
        if (container.getCurrentState() == DragLayout.DRAG_STATE_OPEN) {
            container.closeMenu();
        }
    }

    public void openMenu() {
        if (container.getCurrentState() == DragLayout.DRAG_STATE_CLOSE) {
            container.openMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_layout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_item_search:
                showToast("点击搜索按钮");
                openDatePicker();
                /*Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);*/
                break;
            case R.id.main_sub_item_add:
                showToast("点击了添加好友");
                Intent intent = new Intent(ChatMainActivity.this, GrayService.class);
                startService(intent);
                //SearchFriendActivity.start(this);
                break;
            case R.id.main_sub_item_create:
                showToast("点击了创建群");
                /*Intent selectIntent = new Intent(this, SelectedFriendsActivity.class);
                selectIntent.putExtra("from", "createGroup");
                startActivity(selectIntent);*/
                break;
            case R.id.main_sub_item_settings:
                showToast("点击了设置");
                //SettingsActivity.start(this, Constant.REQUEST_CODE_EDIT_USER_INFO);
                break;
            case R.id.main_sub_item_happy:
                // HappyActivity.startActivity(this);
                break;
            case R.id.main_sub_item_bg:
                showToast("点击了背景");
                /*Intent wallPaperIntent = new Intent(this, WallPaperActivity.class);
                wallPaperIntent.putExtra("from", "wallpaper");
                startActivityForResult(wallPaperIntent, Constant.REQUEST_CODE_SELECT_WALLPAPER);*/
        }
        return true;
    }

    @Override
    public void onDrag(View view, float delta) {
        ViewHelper.setAlpha(bg, delta);
        ViewHelper.setAlpha(icon_1, (1 - delta));
    }

    @Override
    public void onCloseMenu() {
        //当侧滑完全关闭的时候调用
        if (ViewHelper.getAlpha(bg) != 0) {
            ViewHelper.setAlpha(bg, 0);
        }
        if (ViewHelper.getAlpha(icon_1) != 1) {
            ViewHelper.setAlpha(icon_1, 1);
        }
    }

    @Override
    public void onOpenMenu() {
        if (ViewHelper.getAlpha(bg) != 1) {
            ViewHelper.setAlpha(bg, 1);
        }
        if (ViewHelper.getAlpha(icon_1) != 0) {
            ViewHelper.setAlpha(icon_1, 0);
        }
        //当侧滑完全打开的时候调用
    }

    private void openDatePicker() {
        CustomDatePickerDialog dialog = new CustomDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //currentYear = year;
                //currentMonth = month;
                //currentDay = dayOfMonth;
                //updateDateChanged();
                LogUtil.log(getDateFormalFromString(year, month, dayOfMonth));
            }
        }, 2017, 6, 15);
        dialog.show();
    }

    public String getDateFormalFromString(int currentYear, int currentMonth, int currentDay) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(currentYear, currentMonth, currentDay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(gregorianCalendar.getTime());
    }

    @Override
    public void OnNetWorkChanged(boolean isConnected, int type) {
        if (isConnected) {
            //这里判断网络的连接类型
            if (type == ConnectivityManager.TYPE_WIFI) {
                //bindPollService(5);
            } else {
                //bindPollService(6);
            }
            net.setVisibility(View.GONE);
        } else {
            net.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drag_container:
                openMenu();
                break;
            case R.id.rl_menu_head_layout:
//                                点击进入个人信息界面之前，先实时监听个人信息界面中的说说
                /*Intent intent = new Intent(this, UserDetailActivity.class);
                intent.putExtra("from", "me");
                intent.putExtra("uid", UserManager.getInstance().getCurrentUserObjectId());
                startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_USER_INFO);*/
                break;
            case R.id.tv_main_net:
                Intent settingIntent = new Intent();
                settingIntent.setAction(Settings.ACTION_WIFI_SETTINGS);
                startActivity(settingIntent);
                break;
            case R.id.ll_menu_bottom_container:
               /* Intent weatherIntent = new Intent(this, WeatherInfoActivity.class);
                if (mWeatherInfoBean != null) {
                    weatherIntent.putExtra("WeatherInfo", mWeatherInfoBean);
                }
                startActivityForResult(weatherIntent, Constant.REQUEST_CODE_WEATHER_INFO);*/
        }
    }

    @Override
    protected void onDestroy() {
        if (netWorkReceiver != null) {
            unregisterReceiver(netWorkReceiver);
            netWorkReceiver.unregisterListener(this);
            netWorkReceiver = null;
        }
        super.onDestroy();
    }

    public void addOrReplaceFragment(Fragment fragment) {
        addOrReplaceFragment(fragment, 0);
    }

    /**
     * 第一次加载的时候调用该方法设置resId
     *
     * @param fragment
     * @param resId
     */
    public void addOrReplaceFragment(Fragment fragment, int resId) {
        if (resId != 0) {
            fragmentContainerResId = resId;
        }
        if (fragment == null) {
            return;
        }
        if (currentFragment == null) {
            getSupportFragmentManager().beginTransaction().add(resId, fragment).show(fragment).commitAllowingStateLoss();
            currentFragment = fragment;
            return;
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }

    public void showToast(String message) {
        CustomToast.makeText(this, message, Toast.LENGTH_LONG);
    }
}
