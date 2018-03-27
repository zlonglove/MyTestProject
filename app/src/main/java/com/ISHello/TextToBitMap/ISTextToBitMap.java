package com.ISHello.TextToBitMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 将文字转化为位图
 *
 * @author zhanglong
 */

public class ISTextToBitMap {
    private String text;
    private int fontSize;
    private Paint paint;
    private Bitmap bitmap;

    public ISTextToBitMap(String text, int fontsize) {
        this.text = text;
        this.fontSize = fontsize;
        initPaint();
        changeToBitmap();
    }


    private void initPaint() {
        paint = new Paint();
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setTypeface(font);
        paint.setTextSize(fontSize);
    }

    private void changeToBitmap() {
        if ((this.text != null) && (!("".equals(text)))) {
            float width = paint.measureText(this.text);
            Bitmap bitmap = Bitmap.createBitmap((int) width, this.fontSize, Bitmap.Config.ARGB_8888);
            Canvas canvasTemp = new Canvas(bitmap);
            canvasTemp.drawText(text, 0, fontSize - 10, paint);
        }
    }

    /**
     * 获得Bitmap对象
     *
     * @return
     */
    public Bitmap getBitmap() {
        return bitmap;
    }
}
