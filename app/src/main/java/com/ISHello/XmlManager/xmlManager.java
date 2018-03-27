package com.ISHello.XmlManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.util.Log;

import com.ISHello.Pull.ISPullXml;
import com.ISHello.Pull.ISWritePullXml;
import com.ISHello.SAX.SAXForHandler;
import com.ISHello.UserInfo.ISUserInfo;

/**
 * xml解析管理类 里面封装了3中解析xml的方式
 *
 * @author Administrator
 */

/**
 * @author Administrator
 *
 */
public class xmlManager {
    public static final String TAG = "xmlManager";
    public static final int SAX = 0;
    public static final int DOM = 1;
    public static final int PULL = 2;

    public Activity activity;

    public xmlManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param xmlName
     * @param type
     */
    public void read(String xmlName, int type) {
        if (type == SAX) {
            sax(xmlName);
        } else if (type == DOM) {

        } else {
            pull(xmlName);
        }
    }

    public boolean write(String xmlName, int type, List<ISUserInfo> users) {
        if (type == SAX) {

        } else if (type == DOM) {

        } else {
            writeXml(xmlName, users);
        }
        return true;
    }

    private boolean writeXml(String xmlName, List<ISUserInfo> users) {
        Log.i(TAG, "---->File Path==" + activity.getBaseContext().getFilesDir());
        File file = new File(activity.getBaseContext().getFilesDir(), xmlName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ISWritePullXml writePullXml = new ISWritePullXml();
            try {
                writePullXml.save(users, outputStream);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    private void sax(String xmlName) {
        try {
            InputStream in = this.activity.getResources().getAssets()
                    .open(xmlName);
            SAXForHandler sax = new SAXForHandler();
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            try {
                SAXParser saxparser = saxFactory.newSAXParser();
                saxparser.parse(in, sax);
                in.close();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pull(String xmlName) {
        try {
            InputStream in = this.activity.getResources().getAssets()
                    .open(xmlName);
            ISPullXml pullXml = new ISPullXml();
            try {
                pullXml.start(in);
                in.close();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
