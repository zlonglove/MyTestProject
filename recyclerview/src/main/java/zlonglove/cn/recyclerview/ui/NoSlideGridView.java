package zlonglove.cn.recyclerview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author zhanglong
 */
public class NoSlideGridView extends GridView {

    public NoSlideGridView(Context context) {
        super(context);
    }

    public NoSlideGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlideGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
