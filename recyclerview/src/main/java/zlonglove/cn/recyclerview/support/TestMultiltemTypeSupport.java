package zlonglove.cn.recyclerview.support;

/**
 * Created by zhanglong on 2018/4/17.
 */

public class TestMultiltemTypeSupport {
    private int TYPE_HEAD = 0;
    private int TYPE_COMMON = 1;

    MultiItemTypeSupport<String> support = new MultiItemTypeSupport<String>() {

        /**
         * 根据ViewType返回不同的layout
         * @param itemType
         * @return
         */
        @Override
        public int getLayoutId(int itemType) {
            if (itemType == TYPE_HEAD) {
                //return R.layout.item_special;
                return 0;
            } else {
                //return R.layout.item_common;
                return -1;
            }
        }

        /**
         * 根据postion或者数据返回特定的ViewType
         * @param position
         * @param s
         * @return
         */
        @Override
        public int getItemViewType(int position, String s) {
            if (position % 3 == 0 && position > 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_COMMON;
            }
        }
    };
}
