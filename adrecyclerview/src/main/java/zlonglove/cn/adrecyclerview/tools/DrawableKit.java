package zlonglove.cn.adrecyclerview.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * 描述:
 * <p>
 * 创建时间:2017年11月06日 10:32
 *
 * @version 1.0
 */
public class DrawableKit {

    public static Drawable getImgDrawable(int drawableSrc) {
        return getImgDrawable(ContextUtil.getContext(), drawableSrc);
    }

    public static Drawable getImgDrawable(Context context, int drawableSrc) {
        Drawable d = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            d = context.getDrawable(drawableSrc);
        } else {
            d = context.getResources().getDrawable(drawableSrc);
        }
        return d;
    }

    /**
     * 给指定的drawable进行着色
     *
     * @param drawable  待着色的drawable
     * @param tintColor 需要着色的颜色
     */
    public static void setDrawableTintColor(Drawable drawable, int tintColor) {
        if (drawable == null) {
            throw new IllegalArgumentException("需要被着色的drawable不能为空");
        }
        //经测试，安卓4.4以上和一下设置着色的方式不一样
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            DrawableCompat.setTint(drawable, ColorKit.getColorSrc(tintColor));
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
        } else {
            drawable.setColorFilter(ColorKit.getColorSrc(tintColor), PorterDuff.Mode.SRC_ATOP);
        }

    }

    /**
     * 去掉指定的drawable的着色
     *
     * @param drawable
     */
    public static void removeDrawableTintColor(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("需要被着色的drawable不能为空");
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            DrawableCompat.setTint(drawable, Color.TRANSPARENT);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
        } else {
            drawable.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
