package zlonglove.cn.recyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import zlonglove.cn.recyclerview.ItemDecoration.LineItemDecoration;
import zlonglove.cn.recyclerview.R;
import zlonglove.cn.recyclerview.adapter.CommonAdapter;
import zlonglove.cn.recyclerview.adapter.MultiItemCommonAdapter;
import zlonglove.cn.recyclerview.holder.BaseViewHolder;
import zlonglove.cn.recyclerview.support.MultiItemTypeSupport;

public class RecyclerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    private String[] mStrings = {
            "Google", "Hello", "Iron", "Japan", "Coke", "Yahoo", "Sony", "Canon", "Fujitsu", "USA", "Nexus", "LINE", "Haskell", "C++",
            "Java", "Go", "Swift", "Objective-c", "Ruby", "PHP", "Bash", "ksh", "C", "Groovy", "Kotlin"
    };

    private int[] mImageRes = {R.drawable.recycler_image1, R.drawable.recycler_image2, R.drawable.recycler_image3, R.drawable.recycler_image4, R.drawable.recycler_image5, R.drawable.recycler_image6};
    private int TYPE_HEAD = 0;
    private int TYPE_COMMON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        findView();
        init();
    }

    private void findView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        LineItemDecoration lineItemDecoration = new LineItemDecoration(this);
        mRecyclerView.addItemDecoration(lineItemDecoration);

        CommonAdapter<String> adapter = singleSetting();
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(RecyclerActivity.this, mStrings[position], Toast.LENGTH_SHORT).show();
                mRecyclerView.setAdapter(multiSetting());
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 单类型基本设置
     */
    public CommonAdapter<String> singleSetting() {
        List<String> mDatas = Arrays.asList(mStrings);
        CommonAdapter<String> mAdapter = new CommonAdapter<String>(this, R.layout.recycler_item_common, mDatas) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.recycler_tv_content, mDatas.get(position));
                int number = new Random().nextInt(6);
                holder.setImageResource(R.id.recycler_iv_content, mImageRes[number]);
            }
        };
        // mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    /**
     * 多类型基本设置
     */
    public MultiItemCommonAdapter<String> multiSetting() {
        MultiItemTypeSupport<String> support = new MultiItemTypeSupport<String>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_HEAD) {
                    return R.layout.recycler_item_special;
                } else {
                    return R.layout.recycler_item_common;
                }

            }

            @Override
            public int getItemViewType(int position, String s) {
                if (position % 3 == 0 && position > 0) {
                    return TYPE_HEAD;
                } else {
                    return TYPE_COMMON;
                }
            }
        };
        List<String> mDatas = Arrays.asList(mStrings);
        MultiItemCommonAdapter<String> mAdapter = new MultiItemCommonAdapter<String>(this, mDatas, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                if (position % 3 == 0 && position > 0) {
                    holder.setImageResource(R.id.recycler_iv_head, R.drawable.recycler_multi_image);
                } else {
                    holder.setText(R.id.recycler_tv_content, mDatas.get(position));
                    int number = new Random().nextInt(3) + 3;
                    holder.setImageResource(R.id.recycler_iv_content, mImageRes[number]);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }
}
