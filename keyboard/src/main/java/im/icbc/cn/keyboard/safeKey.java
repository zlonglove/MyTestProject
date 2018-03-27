package im.icbc.cn.keyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.regex.Pattern;

public class safeKey extends View {
	private Bitmap m_oBitmap = null;
	private int m_iWidth = 0;
	private int m_iHeight = 0;

	private float m_fRatio1 = 0;
	private float m_fRatio2 = 0;
	
	private final static Pattern PATTERN= Pattern.compile("[\u4e00-\u9fa5]");

	public safeKey(Context context) {
		super(context);

		m_fRatio1 = (float) 35 / 48;
		m_fRatio2 = (float) 35 / 66;
	}

	public safeKey(Context context, AttributeSet attrs) {
		super(context, attrs);

		m_fRatio1 = (float) 35 / 48;
		m_fRatio2 = (float) 35 / 66;
	}

	public safeKey(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		m_fRatio1 = (float) 35 / 48;
		m_fRatio2 = (float) 35 / 66;
	}

	private Canvas initCanvas(int width, int height) {
		if (width < 20 || height < 20 || m_oBitmap != null)
			return null;
		m_oBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		if (m_oBitmap == null)
			return null;
		Canvas canvas = new Canvas();
		canvas.setBitmap(m_oBitmap);
		m_iWidth = width;
		m_iHeight = height;
		return canvas;
	}

	public boolean initKey(int width, int height, Drawable frontbit, Drawable backbit, int backColor, int frontColor) {
		Canvas canvas = initCanvas(width, height);
		if (canvas == null)
			return false;
		drawDrawable(width, height, backbit, backColor, canvas);
		drawDrawable(width, height, frontbit, frontColor, canvas);
		return true;
	}

	public boolean initKey(int width, int height, Bitmap frontbit, Drawable backDrawable, int backColor, int frontColor) {
		Canvas canvas = initCanvas(width, height);
		if (canvas == null)
			return false;
		drawDrawable(width, height, backDrawable, backColor, canvas);
		drawBitmap(width, height, frontbit, frontColor, canvas);
		return true;
	}

	public boolean initKey(int width, int height, String content, Drawable drawable, int backColor, int textColor) {
		Canvas canvas = initCanvas(width, height);
		if (canvas == null)
			return false;
		drawDrawable(width, height, drawable, backColor, canvas);
		drawText(width, height, content, textColor, -1, canvas);
//		Paint p = new Paint();
//		p.setColor(textColor);
////		p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//		p.setTextSize(100);
//		p.setTextAlign(Paint.Align.CENTER);
//		canvas.drawText(content, width/2, height/2+20, p);
		return true;
	}

	public boolean initKey(int width, int height, String content, Bitmap backbit, int backColor, int textColor) {
		Canvas canvas = initCanvas(width, height);
		if (canvas == null)
			return false;
		drawBitmap(width, height, backbit, backColor, canvas);
		drawText(width, height, content, textColor, -1, canvas);
		return true;
	}

	public boolean initKey(int width, int height, String content, Drawable backbit, int backColor, int textColor, int textShadowColor) {
		Canvas canvas = initCanvas(width, height);
		if (canvas == null)
			return false;
		drawDrawable(width, height, backbit, backColor, canvas);
		drawText(width, height, content, textColor, textShadowColor, canvas);
		return true;
	}

	private Canvas newContentInitCanvas() {
		Canvas canvas = new Canvas();
		if (canvas == null || m_oBitmap == null)
			return null;
		canvas.setBitmap(m_oBitmap);
		Paint p = new Paint();
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(p);
		return canvas;
	}

	public boolean setNewContent(String content, Bitmap backbit, int backColor, int textColor) {
		Canvas canvas = newContentInitCanvas();
		if (canvas == null)
			return false;
		drawBitmap(getWidth(), getHeight(), backbit, backColor, canvas);
		drawText(getWidth(), getHeight(), content, textColor, -1, canvas);
		postInvalidate();
		return true;
	}

	public boolean setNewContent(String content, Drawable drawable, int backColor, int textColor) {
		Canvas canvas = newContentInitCanvas();
		if (canvas == null)
			return false;
		drawDrawable(getWidth(), getHeight(), drawable, backColor, canvas);
		drawText(getWidth(), getHeight(), content, textColor, -1, canvas);
		postInvalidate();
		return true;
	}

	public boolean setNewContent(Bitmap frontbit, Drawable backDrawable, int backColor, int frontColor) {
		Canvas canvas = newContentInitCanvas();
		if (canvas == null)
			return false;
		drawDrawable(getWidth(), getHeight(), backDrawable, backColor, canvas);
		drawBitmap(getWidth(), getHeight(), frontbit, frontColor, canvas);
		postInvalidate();
		return true;
	}

	public boolean setNewContent(Drawable frontbit, Drawable backbit, int backColor, int frontColor) {
		Canvas canvas = newContentInitCanvas();
		if (canvas == null)
			return false;
		drawDrawable(getWidth(), getHeight(), backbit, backColor, canvas);
		drawDrawable(getWidth(), getHeight(), frontbit, frontColor, canvas);
		postInvalidate();
		return true;
	}

	public boolean setNewContent(String content, Drawable backbit, int backColor, int textColor, int textShadowColor) {
		Canvas canvas = newContentInitCanvas();
		if (canvas == null)
			return false;
		drawDrawable(getWidth(), getHeight(), backbit, backColor, canvas);
		drawText(getWidth(), getHeight(), content, textColor, textShadowColor, canvas);
		postInvalidate();
		return true;
	}

	private void drawText(int width, int height, String content, int textColor, int textShadowColor, Canvas canvas) {
		if (!TextUtils.isEmpty(content)) {
			
			int textSize = 0;
			
			if (width < height){
				textSize = (int) (width * m_fRatio1 + 0.5f);
			}
			else {
				textSize = (int) (height * m_fRatio2 + 0.5f);
			}

			//匹配是否存在中文
			if(PATTERN.matcher(content).find()){
				
				DisplayMetrics localDisplayMetrics = getContext().getApplicationContext().getResources().getDisplayMetrics();
				if (localDisplayMetrics != null){
					if(	localDisplayMetrics.widthPixels > localDisplayMetrics.heightPixels &&
						localDisplayMetrics.widthPixels > 1000 && localDisplayMetrics.heightPixels < 801){
						textSize = Dp2Px(28f);
					}
					else {
						textSize=Dp2Px(17.5f);
					}				
				}
				else {
					textSize=Dp2Px(17.5f);
				}
			}
			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setColor(textColor);
			paint.setTextSize(textSize);
			int w = (int) (paint.measureText(content) + 0.5f);
			FontMetrics fontMetrics = paint.getFontMetrics();
			int h = (int) (Math.ceil(fontMetrics.descent - fontMetrics.top) - 5);
			canvas.drawText(content, (width - w) / 2, (height - h) / 2 + (int) (-fontMetrics.ascent), paint);
		}
	}

	private void drawBitmap(int width, int height, Bitmap backbit, int backColor, Canvas canvas) {
		if (backbit == null) {
			canvas.drawColor(backColor);
		} else {
			byte[] chunk = backbit.getNinePatchChunk();
			if (chunk != null) {
				Rect desRange = new Rect(0, 0, width, height);
				NinePatch np = new NinePatch(backbit, chunk, null);
				np.draw(canvas, desRange, new Paint());
			} else {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setDither(true);
				Rect srcRange = new Rect(0, 0, backbit.getWidth(), backbit.getHeight());
				Rect desRange = new Rect(0, 0, width, height);
				canvas.drawBitmap(backbit, srcRange, desRange, paint);
			}
		}
	}

	private void drawDrawable(int width, int height, Drawable drawable, int backColor, Canvas canvas) {
		if (drawable == null) {
			canvas.drawColor(backColor);
		} else {
			drawable.setBounds(new Rect(0, 0, width, height));
			drawable.draw(canvas);
		}
	}

	public void cleanMemory() {
		if (m_oBitmap != null)
			m_oBitmap.recycle();
		m_oBitmap = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		if (m_oBitmap != null) {
			canvas.drawBitmap(m_oBitmap, 0, 0, paint);
		}
	}

	private int Dp2Px(float dp) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

}
