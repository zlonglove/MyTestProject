package im.icbc.com.indexbarlayout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import im.icbc.com.indexbarlayout.IndexBar;
import im.icbc.com.indexbarlayout.IndexLayout;
import im.icbc.com.indexbarlayout.R;
import im.icbc.com.indexbarlayout.RecCarAdapter;
import im.icbc.com.indexbarlayout.Utils;
import im.icbc.com.indexbarlayout.datas.CarBean;
import im.icbc.com.indexbarlayout.stickyheader.NormalDecoration;

public class barIndexLayoutActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_index_layout);
        setTitle("车型选择");
        mRecyclerView = (RecyclerView) findViewById(R.id.recView);
        CarBean carBean = new Utils().readFromAssets(barIndexLayoutActivity.this);
        final List<CarBean.CarInfo> carList = carBean.getData();


        RecCarAdapter adapter = new RecCarAdapter(this);
        adapter.addDatas(carList);

        final NormalDecoration decoration = new NormalDecoration() {
            @Override
            public String getHeaderName(int pos) {
                return carList.get(pos).getInitial();
            }
        };

        decoration.setOnHeaderClickListener(new NormalDecoration.OnHeaderClickListener() {
            @Override
            public void headerClick(int pos) {
                Toast.makeText(barIndexLayoutActivity.this, "点击到头部" + carList.get(pos).getInitial(), Toast.LENGTH_SHORT).show();
            }
        });

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.addItemDecoration(decoration);////添加分割
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mRecyclerView.setLayoutManager(manager);////设置布局管理器
        mRecyclerView.setAdapter(adapter);


        //侧边导航栏
        IndexLayout indexLayout = (IndexLayout) findViewById(R.id.index_layout);
        List<String> heads = new ArrayList<>();
        for (CarBean.CarInfo car : carList) {
            if (!heads.contains(car.getInitial())) {
                heads.add(car.getInitial());
            }
        }
        indexLayout.setIndexBarHeightRatio(0.9f);
        indexLayout.getIndexBar().setIndexsList(heads);
        indexLayout.getIndexBar().setIndexChangeListener(new IndexBar.IndexChangeListener() {
            @Override
            public void indexChanged(String indexName) {
                for (int i = 0; i < carList.size(); i++) {
                    if (indexName.equals(carList.get(i).getInitial())) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

    }
}
