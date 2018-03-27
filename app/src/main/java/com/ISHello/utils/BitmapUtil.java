package com.ISHello.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.util.Log;

public class BitmapUtil {
    private final static String TAG = "BitmapUtil";

    /**
     * 等比压缩图片
     *
     * @param sourceBitmap ---原始图片的bitmap
     * @param LimitSize    ---限制最大区域
     * @param quality      0--100
     * @return
     */
    public static byte[] getZoomImage(Bitmap sourceBitmap, int LimitSize, int quality) {
        if (sourceBitmap == null) {
            return null;
        }
        if (LimitSize <= 0) {
            Log.i(TAG, "--->limit size can not <=0");
            return null;
        }
        InputStream input = null;
        try {
            int sourceWidth = sourceBitmap.getWidth();
            int sourceHeight = sourceBitmap.getHeight();
            float scale = 1f;

            if (sourceWidth > sourceHeight) {
                if (sourceWidth > LimitSize) {
                    scale = LimitSize * 1.0f / sourceWidth;
                }
            } else {
                if (sourceHeight > LimitSize) {
                    scale = LimitSize * 1.0f / sourceHeight;
                }
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality, os);
            // bitmap.recycle();
            bitmap = null;
            return os.toByteArray();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 这个是等比例缩放
     *
     * @param sourceBitmap
     * @param widthLimit
     * @param heightLimit
     * @param isThumbnail  --是否是缩略图[true-是,false-不是]
     * @param quality
     * @return
     */
    public static byte[] getZoomImage(Bitmap sourceBitmap, int widthLimit, int heightLimit, boolean isThumbnail, int quality) {
        if (sourceBitmap == null) {
            return null;
        }
        if (widthLimit <= 0 || heightLimit <= 0) {
            Log.i(TAG, "--->limit size can not <=0");
            return null;
        }
        InputStream input = null;
        try {
            int sourceWidth = sourceBitmap.getWidth();
            int sourceHeight = sourceBitmap.getHeight();
            float scale = 1f;
            if (isThumbnail) {
                if (sourceWidth > sourceHeight) {
                    scale = widthLimit * 1.0f / sourceWidth;
                    if (scale * sourceHeight > heightLimit) {
                        scale = heightLimit * 1.0f / sourceHeight;
                    }
                } else {
                    scale = heightLimit * 1.0f / sourceHeight;
                    if (scale * sourceWidth > widthLimit) {
                        scale = widthLimit * 1.0f / sourceWidth;
                    }
                }
            } else {
                if (sourceWidth > sourceHeight) {
                    if (sourceWidth > widthLimit) {
                        scale = widthLimit * 1.0f / sourceWidth;
                    }
                } else {
                    if (sourceHeight > heightLimit) {
                        scale = heightLimit * 1.0f / sourceHeight;
                    }
                }
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality, os);
            // bitmap.recycle();
            bitmap = null;
            return os.toByteArray();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * @param path --图片路径
     * @param size --宽高限制
     * @return
     * @throws IOException
     */
    public static Bitmap revitionImageSize(String path, int size) throws IOException {
        if (TextUtils.isEmpty(path) || size <= 0) {
            return null;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不为Bitmap分配内存空间,只记录一些改图片的信息
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {// 这一步是根据要设置的大小，使宽高都能满足，outWidth每次除以2(i次),是不是小于等于size
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);// 这个参数表示新生成的图片是原始图片的几分之一(2的i次方)
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap revitionImageSize(File file, int size) throws IOException {
        if (file == null || size < 0) {
            return null;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
                in = new BufferedInputStream(new FileInputStream(file));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap revitionImageSize(byte[] data, int size) throws IOException {
        if (data == null || size <= 0 || data.length <= 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param scale
     * @return A bitmap that represents the specified subset of source
     */
    public static Bitmap zoom(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * @param bitmap
     * @param scaleX
     * @param scaleY
     * @return A bitmap that represents the specified subset of source
     */
    public static Bitmap zoom(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        // RCB means
        // Rounded
        // Corner Bitmap
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }

    public static boolean compress(String srcPath, int maxLength, int maxSize) {
        File oldFile = new File(srcPath);

        if (oldFile.length() < maxSize * 2 * 1024 / 3) {
            return copyFile(oldFile, oldFile);
        } else {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(srcPath, newOpts);
            newOpts.inJustDecodeBounds = false;
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            int option = 1;
            if (w > maxLength || h > maxLength) {
                option = Math.max(w / maxLength, h / maxLength);
            }

            if (option <= 0)
                option = 1;
            newOpts.inSampleSize = option;
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            return compressImage(bitmap, maxSize, oldFile);// 压缩好比例大小后再进行质量压缩
        }
    }

    public static boolean compressImage(Bitmap bitmap, int size, File file) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        if ((float) out.toByteArray().length > size * 1024) {
            float zoom = (float) Math.sqrt(size * 1024 / (float) out.toByteArray().length);

            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);

            Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 98, out);
            while (out.toByteArray().length > size * 1024) {
                matrix.setScale(0.9f, 0.9f);
                result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
                out.reset();
                result.compress(Bitmap.CompressFormat.JPEG, 98, out);
            }
            result.recycle();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            out.writeTo(fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap.recycle();
        }
        return false;
    }

    public static boolean copyFile(File oldFile, File newFile) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        FileChannel in = null;

        FileChannel out = null;
        try {
            fis = new FileInputStream(oldFile);
            fos = new FileOutputStream(newFile);

            in = fis.getChannel();
            out = fos.getChannel();

            in.transferTo(0, in.size(), out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 从Bitmap获取byte[]
     *
     * @param bm
     * @return 字节数组
     */
    public static byte[] bmpToByteArray(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        InputStream input = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG, 100, os);
            bm.recycle();
            return os.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param bm
     * @param quality Hint to the compressor, 0-100. 0 meaning compress for small
     *                size, 100 meaning compress for max quality. Some formats, like
     *                PNG which is lossless, will ignore the quality setting
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bm, int quality) {
        if (bm == null) {
            return null;
        }
        InputStream input = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG, quality, os);
            bm.recycle();
            return os.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param sourceBitmap ---图片Bitmap
     * @param limitSize    ---图片大小限制,以K为单位
     * @return
     */
    public static byte[] bmpCompressedBytes(Bitmap sourceBitmap, int limitSize) {
        if (sourceBitmap == null) {
            return null;
        }
        int quality = 95;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        sourceBitmap.compress(CompressFormat.JPEG, quality, os);
        while (os.toByteArray().length > 1024 * limitSize) {
            os.reset();
            quality = quality - 10;
            sourceBitmap.compress(CompressFormat.JPEG, quality, os);
        }
        return os.toByteArray();
    }

}
