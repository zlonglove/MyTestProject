package com.ISHello.PickView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ISHello.base.base.BaseActivity;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.ishelloword.R;

import java.util.ArrayList;

/**
 * @author
 */
public class PickViewActivity extends BaseActivity {
    private OptionsPickerView pvOptions;
    
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private TextView tv_pick_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_view);
        setTitle("地区选择");
        findView();
        getOptionData();
        initOptionPicker();
    }

    private void findView() {
        tv_pick_view = findViewById(R.id.tv_pick_view);
        tv_pick_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvOptions != null && !pvOptions.isShowing()) {
                    pvOptions.show();
                }
            }
        });
    }

    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        //选项1
        options1Items.add(new ProvinceBean(0, "广东", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(1, "湖南", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(2, "广西", "描述部分", "其他数据"));

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items_02.add("株洲");
        options2Items_02.add("衡阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items_03.add("玉林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
    }

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        ":" + options2Items.get(options1).get(options2)
                        /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                tv_pick_view.setText(tx);
            }
        }).setTitleText("地区选择")//标题文字
                .isDialog(true)//是否是对话框模式
                .setOutSideCancelable(true)//是否能取消
                .setContentTextSize(15)//设置滚轮文字大小[内容文字大小]
                .setLineSpacingMultiplier(2)//条目间距倍数 默认1.6
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)//滚轮背景颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色
                .setTitleColor(Color.LTGRAY)//标题颜色
                .setCancelColor(0xFF7F7F7F)//取消按钮颜色
                .setSubmitColor(0xFF3474DA)//确定按钮颜色
                .setTextColorCenter(0xFF3A3A3B)//分割线之间的文字颜色
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                       /* String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();*/
                    }
                })
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

        pvOptions.show();
    }
}
