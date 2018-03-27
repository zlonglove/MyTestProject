package com.ISHello.Enum;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String url = "http://www.baidu.com";
        GetType(url);
    }

    public static void GetType(String url) {
        switch (Scheme.ofUri(url)) {
            case HTTP:
                info(Scheme.HTTP);
                break;
            case HTTPS:
                info(Scheme.HTTPS);
                break;
            case ASSETS:
                info(Scheme.ASSETS);
                break;
            case CONTENT:
                info(Scheme.CONTENT);
                break;
            case FILE:
                info(Scheme.FILE);
                break;
            case DRAWABLE:
                info(Scheme.DRAWABLE);
                break;
            case UNKNOWN:
                info(Scheme.UNKNOWN);
                break;
            default:
                break;
        }
    }

    public static void info(Scheme info) {
        System.out.println("--->type==" + info.ordinal());
    }

}
