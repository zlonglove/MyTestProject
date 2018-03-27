package com.ISHello.ImageLoader.implement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by zhanglong on 2017/4/20.
 */

public interface ImageDownloader {
    /**
     * Retrieves {@link InputStream} of image by URI.
     *
     * @param imageUri Image URI
     * @param extra    Auxiliary object which was passed to; can be null
     * @return {@link InputStream} of image
     * @throws IOException                   if some I/O error occurs during getting image stream
     * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
     */
    InputStream getStream(String imageUri, Object extra) throws IOException;

    /**
     * Represents supported schemes(protocols) of URI. Provides convenient methods for work with schemes and URIs.
     */
    public enum Scheme {
        HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");

        private String scheme;
        private String uriPrefix;

        Scheme(String scheme) {
            this.scheme = scheme;
            uriPrefix = scheme + "://";
        }

        /**
         * Defines scheme of incoming URI
         *
         * @param uri URI for scheme detection
         * @return Scheme of incoming URI
         */
        public static Scheme ofUri(String uri) {
            if (uri != null) {
                for (Scheme s : values()) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
        }

        /**
         * Appends scheme to incoming path
         */
        public String wrap(String path) {
            return uriPrefix + path;
        }

        /**
         * Removed scheme part ("scheme://") from incoming URI
         */
        public String crop(String uri) {
            if (!belongsTo(uri)) {
                throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
            }
            return uri.substring(uriPrefix.length());
        }
    }
}
