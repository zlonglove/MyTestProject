package com.ISHello.ResideMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ISHello.ResideMenu.utils.FileUtil;
import com.ISHello.ViewPage.FirstFragment;
import com.ISHello.ViewPage.SecondFragment;
import com.ISHello.ViewPage.ThirdFragment;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

import java.io.IOException;

public class ResideMenuActivity extends BaseActivity implements View.OnClickListener {
    private Button[] mTabs;
    ImageView iv_recent_tips, iv_contact_tips;
    private int index;
    private int currentTabIndex;
    private FirstFragment contactFragment;
    private SecondFragment recentFragment;
    private ThirdFragment settingFragment;
    private Fragment[] fragments;

    public ResideMenu resideMenu;
    private TextView signText;
    ResideMenuItem item[] = new ResideMenuItem[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residemenu_layout);
        initResideMenu();

        initView();
        initTab();
    }

    private void initResideMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.left_layout_bg);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.55f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        resideMenu.scrollViewLeftMenu.findViewById(R.id.mine_avatar).setOnClickListener(avatarListener);
        resideMenu.scrollViewLeftMenu.findViewById(R.id.mine_sign_relative).setOnClickListener(signOnClickListener);
        signText = (TextView) resideMenu.scrollViewLeftMenu.findViewById(R.id.sign_content);
        try {
            signText.setText(FileUtil.readSignFromFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create menu items;
        String titles[] = {"我的钱包", "我的收藏", "我的相册", "关于我"};
        int icon[] = {R.drawable.qq_setting_qianbao, R.drawable.qq_setting_shoucang, R.drawable.qq_setting_xiangce, R.drawable.mine_avatar};

        for (int i = 0; i < titles.length; i++) {
            item[i] = new ResideMenuItem(this, icon[i], titles[i]);
            item[i].setOnClickListener(this);
            resideMenu.addMenuItem(item[i], ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
    }

    private void initView() {
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_message);
        mTabs[1] = (Button) findViewById(R.id.btn_contract);
        mTabs[2] = (Button) findViewById(R.id.btn_set);
        iv_recent_tips = (ImageView) findViewById(R.id.iv_recent_tips);
        iv_contact_tips = (ImageView) findViewById(R.id.iv_contact_tips);

        mTabs[0].setSelected(true);
    }

    private void initTab() {
        contactFragment = new FirstFragment();
        recentFragment = new SecondFragment();
        settingFragment = new ThirdFragment();
        fragments = new Fragment[]{recentFragment, contactFragment, settingFragment};

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        trx.replace(R.id.fragment_container, recentFragment).commit();
    }

    private View.OnClickListener avatarListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            LogUtil.log("SetMyInfo Click");
            // TODO Auto-generated method stub
           /* Intent intent =new Intent(MainActivity.this,SetMyInfoActivity.class);
            intent.putExtra("from", "me");
            startActivity(intent);*/
        }
    };

    private View.OnClickListener signOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            LogUtil.log("EditSign Click");
            // TODO Auto-generated method stub
            /*Intent intent =new Intent(MainActivity.this,EditSignActivity.class);
            startActivityForResult(intent, 0);*/
        }
    };


    @Override
    public void onClick(View v) {
        LogUtil.log("left button onClick()");
    }

    /**
     * 底部按钮按下
     *
     * @param view
     */
    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_message:
                index = 0;
                break;
            case R.id.btn_contract:
                index = 1;
                break;
            case R.id.btn_set:
                index = 2;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            if (index > currentTabIndex) {//右滑
                trx.setCustomAnimations(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
            } else {
                trx.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            }
            trx.replace(R.id.fragment_container, fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}