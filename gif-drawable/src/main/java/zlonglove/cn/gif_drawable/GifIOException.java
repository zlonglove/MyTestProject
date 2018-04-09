package zlonglove.cn.rxjava.ui;

/**
 * Created by zhanglong on 2018/4/8.
 */
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Exception encapsulating {@link GifError}s.
 *
 * @author koral--
 */
public class GifIOException extends IOException {
    private static final long serialVersionUID = 13038402904505L;
    /**
     * Reason which caused an exception
     */
    @NonNull
    public final GifError reason;

    private final String mErrnoMessage;

    @Override
    public String getMessage() {
        if (mErrnoMessage == null) {
            return reason.getFormattedDescription();
        }
        return reason.getFormattedDescription() + ": " + mErrnoMessage;
    }

    private GifIOException(int errorCode, String errnoMessage) {
        reason = GifError.fromCode(errorCode);
        mErrnoMessage = errnoMessage;
    }

    static GifIOException fromCode(final int nativeErrorCode) {
        if (nativeErrorCode == GifError.NO_ERROR.errorCode) {
            return null;
        }
        return new GifIOException(nativeErrorCode, null);
    }
}