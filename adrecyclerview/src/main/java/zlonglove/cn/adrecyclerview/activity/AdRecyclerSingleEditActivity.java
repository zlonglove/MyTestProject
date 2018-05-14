package zlonglove.cn.adrecyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.ArrayList;
import java.util.List;

import zlonglove.cn.adrecyclerview.R;
import zlonglove.cn.adrecyclerview.adapter.MenuHeaderRecyclerGridAdapter;
import zlonglove.cn.adrecyclerview.base.OnRecyclerItemClickListener;
import zlonglove.cn.adrecyclerview.entity.MenuItem;
import zlonglove.cn.adrecyclerview.helper.MenuHelper;
import zlonglove.cn.adrecyclerview.tools.ContextUtil;

public class AdRecyclerSingleEditActivity extends AppCompatActivity implements OnRecyclerItemClickListener<MenuItem> {

    private RecyclerView mRecyclerView;

    private List<MenuItem> mFavList;

    private RecyclerViewDragDropManager mDragDropManager;
    private MenuHeaderRecyclerGridAdapter adapter;//原装适配器
    private RecyclerView.Adapter dragAdapter;//对原装适配器封装包裹后的实现了拖拽功能的适配器
    private int itemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextUtil.init(getApplicationContext());
        setContentView(R.layout.activity_ad_recycler_view);
        initView();
        initEvents();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_display);
    }

    private void initEvents() {
        initData();
        adapter = new MenuHeaderRecyclerGridAdapter(mFavList, mRecyclerView, true);
        adapter.setOnRecyclerItemClickListener(this);
        adapter.setOnDeleteClickListener(new MenuHeaderRecyclerGridAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, MenuItem item, int position) {
                Toast.makeText(AdRecyclerSingleEditActivity.this, "从最爱里面移除" + item.getName(), Toast.LENGTH_SHORT).show();
                notifyFavDataRemoved(item);
            }
        });
        mDragDropManager = new RecyclerViewDragDropManager();
        mDragDropManager.setItemMoveMode(itemMoveMode);
        // Start dragging after long press
        mDragDropManager.setInitiateOnLongPress(true);
        mDragDropManager.setOnItemDragEventListener(null);
        mDragDropManager.setInitiateOnMove(false);
        mDragDropManager.setLongPressTimeout(750);
        mDragDropManager.setOnItemDragEventListener(new RecyclerViewDragDropManager.OnItemDragEventListener() {
            @Override
            public void onItemDragStarted(int position) {

            }

            @Override
            public void onItemDragPositionChanged(int fromPosition, int toPosition) {

            }

            @Override
            public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {

            }

            @Override
            public void onItemDragMoveDistanceUpdated(int offsetX, int offsetY) {

            }
        });
        // setup dragging item effects (NOTE: DraggableItemAnimator is required)
//        dragDropManager.setDragStartItemAnimationDuration(250);
//        dragDropManager.setDraggingItemAlpha(0.8f);
        mDragDropManager.setDraggingItemScale(1.1f);
//        dragDropManager.setDraggingItemRotation(15.0f);
        dragAdapter = mDragDropManager.createWrappedAdapter(adapter);
        mRecyclerView.setAdapter(dragAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 4));
        GeneralItemAnimator itemAnimator = new DraggableItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        mDragDropManager.attachRecyclerView(mRecyclerView);//关键步骤，设置好DragDropManager和RecyclerView后将二者绑定实现拖拽功能


    }

    @Override
    public void onItemClick(View v, MenuItem item, int position, int segment) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据列表
     */
    private void initData() {
        if (mFavList != null) {
            mFavList.clear();
        } else {
            mFavList = new ArrayList<>();
        }
        mFavList.addAll(MenuHelper.parseJSONData());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void notifyFavDataRemoved(MenuItem item) {
        if(adapter!=null){
            adapter.getRecyclerItems().remove(item);
            adapter.notifyDataSetChanged();
        }
    }
}
