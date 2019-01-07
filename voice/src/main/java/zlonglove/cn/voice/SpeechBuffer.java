package zlonglove.cn.voice;

/**
 *
 */

public class SpeechBuffer {

    boolean close;
    byte[] buffer;
    int offset;
    int len;

    SpeechBuffer(boolean close) {
        this.close = close;
    }

    SpeechBuffer(byte[] buffer, int offset, int len) {
        this.close = false;
        this.buffer = buffer;
        this.offset = offset;
        this.len = len;
    }
}
