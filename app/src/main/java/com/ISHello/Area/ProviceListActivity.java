package com.ISHello.Area;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ISHello.DAO.SystemAreaDao;
import com.ISHello.base.base.BaseActivity;
import com.ISHello.utils.LogUtil;
import com.example.ishelloword.R;

import java.util.ArrayList;

public class ProviceListActivity extends BaseActivity {
    public final String INTENT_PROVINCE_NAME = "province_name";
    public final String INTENT_CITY_NAME = "city_name";

    /**
     * Constants for 'Activity result code'
     */
    public final int RESULT_NO_CITIES = 0x0100;
    public final int RESULT_NORMAL = 0x1000;

    private ListView mListView;
    private Context mContext;
    private CityListItemAdapter mAdapter;

    ArrayList<String> mProvinceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provice_list);
        initData();
        findViews();
        initViews();
    }

    protected void initData() {
        mContext = this;
        mProvinceList = SystemAreaDao.getInstance().querySystemProvinceList();
        mAdapter = new CityListItemAdapter(this, mProvinceList);
    }

    protected void findViews() {
        mListView = (ListView) findViewById(R.id.activity_provincelist_lv);
    }

    protected void initViews() {
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mProvinceList.get(position);
                Intent i = new Intent(mContext, CityListActivity.class);
                i.putExtra("province_name", name);
                startActivityForResult(i, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String cityName = "", provinceName;
        switch (resultCode) {
            case RESULT_NO_CITIES:
                provinceName = data.getStringExtra(INTENT_PROVINCE_NAME);
                showToast(provinceName, Toast.LENGTH_SHORT);
                commitDistrict(provinceName, cityName);
                //returnActivityResult(provinceName, cityName);
                break;
            case RESULT_NORMAL:
                provinceName = data.getStringExtra(INTENT_PROVINCE_NAME);
                cityName = data.getStringExtra(INTENT_CITY_NAME);
                showToast(provinceName + "/" + cityName,Toast.LENGTH_SHORT);
                commitDistrict(provinceName, cityName);
                //returnActivityResult(provinceName, cityName);
                break;
            default:
                break;
        }
    }

    private void commitDistrict(final String provinceName, final String cityName) {
        int serialNo = SystemAreaDao.getInstance().querySystemSerial(provinceName, cityName);
        final String serialNoStr = String.valueOf(serialNo);
        final String areaCode = SystemAreaDao.getInstance().queryICBCAreaCodeBySerialNo(serialNo);
        LogUtil.log("seriaNo==" + serialNoStr + "/areaCode==" + areaCode);
    }

}
