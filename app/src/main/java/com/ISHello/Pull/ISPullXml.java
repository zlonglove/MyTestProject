package com.ISHello.Pull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ISHello.UserInfo.ISUserInfo;

import android.util.Log;
import android.util.Xml;

/**
 * <?xml version="1.0" encoding="UTF-8"?>
 * <persons>
 * <person id="1">
 * <name>zhanglong</name>
 * <age>24</age>
 * </person>
 * <person id="2">
 * <name>zhangrong</name>
 * <age>21</age>
 * </person>
 * <person id="3">
 * <name>zhangbangyou</name>
 * <age>24</age>
 * </person>
 * </persons>
 *
 * @author zhanglong
 */
public class ISPullXml {
    class node {
        public static final String persons = "persons";
        public static final String person = "person";
        public static final String name = "name";
        public static final String age = "age";
    }

    private final String TAG = "ISPullXml";
    private List<ISUserInfo> users;
    ISUserInfo user;
    XmlPullParser pullparser;

    public boolean start(InputStream in) throws XmlPullParserException, IOException {
        pullparser = Xml.newPullParser();
        pullparser.setInput(in, "UTF-8");
        int type = pullparser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_DOCUMENT:
                    Log.i(TAG, "-->START_DOCUMENT--");
                    users = new ArrayList<ISUserInfo>();
                    break;
                case XmlPullParser.START_TAG:
                    Log.i(TAG, "-->START_TAG--");
                    try {
                        parserTag();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if (node.person.equals(pullparser.getName())) {
                        users.add(user);
                        user = null;
                    }
                    break;

                default:
                    break;
            }
            type = pullparser.next();
        }
        return true;
    }

    void parserTag() throws XmlPullParserException, IOException {
        if (node.persons.equals(pullparser.getName())) {
            Log.i(TAG, "--->This is persions node");
        }

        if (node.person.equals(pullparser.getName())) {
            Integer id = new Integer(pullparser.getAttributeValue(0));
            user = new ISUserInfo();
            user.setId(id);
            Log.i(TAG, "--->This is person node--" + id);
        }

        if (node.name.equals(pullparser.getName())) {
            if (user != null) {
                String name = pullparser.nextText();
                user.setName(name);
                Log.i(TAG, "--->This is name node--" + name);
            }
        }

        if (node.age.equals(pullparser.getName())) {

            if (user != null) {
                String age = pullparser.nextText();
                user.setAge(new Short(age));
                Log.i(TAG, "--->This is age node--" + age);
            }

        }
    }

    public List<ISUserInfo> getUsersInfo() {
        return users;
    }

}
