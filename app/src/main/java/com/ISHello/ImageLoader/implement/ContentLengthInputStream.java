package com.ISHello.ImageLoader.implement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhanglong on 2017/4/20.
 */

public class ContentLengthInputStream extends InputStream {

    private final InputStream stream;
    private final int length;

    public ContentLengthInputStream(InputStream stream, int length) {
        this.stream = stream;
        this.length = length;
    }

    @Override
    public int available() {
        return length;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public void mark(int readLimit) {
        stream.mark(readLimit);
    }

    @Override
    public int read() throws IOException {
        return stream.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return stream.read(buffer);
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return stream.read(buffer, byteOffset, byteCount);
    }

    @Override
    public void reset() throws IOException {
        stream.reset();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        return stream.skip(byteCount);
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }
}
