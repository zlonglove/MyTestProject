package org.loader.dropedittext;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.loader.view.DropEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import zlonglove.cn.recyclerview.R;
import zlonglove.cn.recyclerview.bean.ShareMenuItem;

public class DropActivity extends AppCompatActivity {
    private Dialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_main);

        DropEditText drop1 = (DropEditText) findViewById(R.id.drop_edit);
        DropEditText drop2 = (DropEditText) findViewById(R.id.drop_edit2);

        drop1.setAdapter(new BaseAdapter() {
            private List<String> mList = new ArrayList<String>() {
                {
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");

                }
            };

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(DropActivity.this);
                tv.setText(mList.get(position));
                return tv;
            }
        });

        drop2.setAdapter(new BaseAdapter() {
            private List<String> mList = new ArrayList<String>() {
                {
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                    add("常用选项 one");
                    add("常用选项 two");
                    add("常用选项 three");
                    add("常用选项 four");
                    add("常用选项 five");
                    add("常用选项 six");
                    add("常用选项 7怎么拼来着？");
                }
            };

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(DropActivity.this);
                tv.setText(mList.get(position));
                return tv;
            }
        });

        Log.i("zhanglong", "--->2015/04/12 12:33 " + isValidDate("2015/04/12 12:33"));
        Log.i("zhanglong", "--->2015/13/12 12:33 " + isValidDate("2015/04/1212:33"));
        Log.i("zhanglong", "--->2015/02/29 12:33 " + isValidDate("2015/02/29 12:33"));
        Log.i("zhanglong", "--->2015/02/05 13:33 " + isValidDate("2015/02/05 13:33"));

        showBottomDialog();
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写, HH:mm 24小时制
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    private void showBottomDialog() {
        /*if (bottomDialog == null) {
            bottomDialog = new Dialog(this, R.style.QrcodeBottomDialog);
            View contentView = LayoutInflater.from(this).inflate(R.layout.qrcode_bottom_dialog, null);

            final GridView mGridView = (GridView) contentView.findViewById(R.id.financial_common_recyclerview);
            mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            List<ShareMenuItem> shareMenuItems = new ArrayList<>();
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));
            shareMenuItems.add(new ShareMenuItem("朋友圈", R.drawable.qr_vchatfriends_share));

            FinancialCommonAdapter financialCommonAdapter = new FinancialCommonAdapter(this, shareMenuItems);
            mGridView.setAdapter(financialCommonAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (bottomDialog != null && bottomDialog.isShowing()) {
                        bottomDialog.dismiss();
                    }
                }
            });
            bottomDialog.setContentView(contentView);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels - dp2px(this, 16f);
            params.bottomMargin = dp2px(this, 8f);
            contentView.setLayoutParams(params);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }*/
        if (bottomDialog == null) {
            bottomDialog = new Dialog(this, R.style.QrcodeBottomDialog);
            View contentView = LayoutInflater.from(this).inflate(R.layout.qrcode_share_layout, null);
            bottomDialog.setContentView(contentView);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels - dp2px(this, 16f);
            params.bottomMargin = dp2px(this, 8f);
            contentView.setLayoutParams(params);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
        bottomDialog.show();
    }

    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }

    class FinancialCommonAdapter extends BaseAdapter {
        private List<ShareMenuItem> mList;
        private Context mContext;

        public FinancialCommonAdapter(Context context, List<ShareMenuItem> list) {
            mContext = context;
            mList = list;
        }

        public void update(List<ShareMenuItem> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            }

            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.financial_common_list_item, null);
                holder = new ViewHolder();
                holder.icom = (ImageView) convertView.findViewById(R.id.financial_common_item_icon);
                holder.title = (TextView) convertView.findViewById(R.id.financial_common_item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShareMenuItem shareMenuItem = mList.get(position);
            holder.title.setText(shareMenuItem.getTitle());
            holder.icom.setImageResource(shareMenuItem.getResourceId());
            return convertView;
        }


        private class ViewHolder {
            ImageView icom;
            TextView title;
        }

    }
}
