package zlonglove.cn.recyclerview.support;

/**
 * Created on 2018/04/17.
 */

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
