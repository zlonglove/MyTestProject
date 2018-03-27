package com.ISHello.Area;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ISHello.DAO.SystemAreaDao;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.List;

public class CityListActivity extends BaseActivity {
    public final String INTENT_PROVINCE_NAME = "province_name";
    public final String INTENT_CITY_NAME = "city_name";

    /**
     * Constants for 'Activity result code'
     */
    public final int RESULT_NO_CITIES = 0x0100;
    public final int RESULT_NORMAL = 0x1000;

    private ListView mLvCities;
    //	private Button mButtonBack;
    private CityListItemAdapter mAdapter;

    private List<String> mCityList;
    private String mProvinceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initData();
        findViews();
        initViews();
    }

    protected void initData() {
        Intent intent = getIntent();
        mProvinceName = intent.getStringExtra("province_name");

        mCityList = SystemAreaDao.getInstance().querySystemCityList(mProvinceName);

        if (mCityList == null || mCityList.size() == 0) {
            returnActivityResult(RESULT_NO_CITIES, "");
        }
        mAdapter = new CityListItemAdapter(CityListActivity.this, mCityList);
    }

    protected void findViews() {
        mLvCities = (ListView) findViewById(R.id.activity_citylist_lv);
    }

    protected void initViews() {
        mLvCities.setAdapter(mAdapter);
        mLvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mCityList.get(position);
                returnActivityResult(RESULT_NORMAL, name);
            }
        });
    }

    void returnActivityResult(int resultCode, String cityName) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_PROVINCE_NAME, mProvinceName);
        intent.putExtra(INTENT_CITY_NAME, cityName);
        setResult(resultCode, intent);
        finish();
    }
}
