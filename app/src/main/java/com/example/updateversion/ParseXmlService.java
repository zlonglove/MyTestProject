package com.example.updateversion;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author zhanglong
 */
public class ParseXmlService {

    public HashMap<String, String> parseXml(InputStream inputStream) {
        HashMap<String, String> hashMap = null;
        boolean flag = true;
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream, "UTF-8");
            int event = pullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        hashMap = new HashMap<String, String>();
                        break;
                    case XmlPullParser.START_TAG:
                        flag = true;
                        String name = pullParser.getName();
                        if ("VERSIONCODE".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("versionCode", pullParser.nextText().trim());
                        } else if ("FILENAME".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("fileName", pullParser.nextText().trim());
                        } else if ("LOADURL".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("loadUrl", pullParser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        flag = false;
                        break;
                }
                event = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

}
