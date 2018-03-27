package com.ISHello.SAX;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ISHello.UserInfo.ISUserInfo;

import android.util.Log;

/**
 * Simple API for XML
 * 以事件驱动为xml API，解析速度快,占用的内存小
 *
 * @author Administrator
 */
public class SAXForHandler extends DefaultHandler {

    private final String TAG = "SAXForHandler";
    private List<ISUserInfo> users;
    /**
     * 记录前一个标签的值
     */
    String preTag;
    ISUserInfo user;

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.startDocument();
        users = new ArrayList<ISUserInfo>();
        Log.i(TAG, "--->startDocument()");
    }


    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
        Log.i(TAG, "--->endDocument()");
        for (int i = 0; i < users.size(); i++) {
            Log.i(TAG, "--->id==" + users.get(i).getId()
                    + "--->name==" + users.get(i).getName()
                    + "--->age==" + users.get(i).getAge()
            );
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        super.startElement(uri, localName, qName, attributes);
        Log.i(TAG, "--->" + qName + "--startElement--");
        if ("person".equals(localName)) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Log.i(TAG, "--->localName--" + attributes.getLocalName(i)
                        + "---->value--" + attributes.getValue(i));
                user = new ISUserInfo();
                user.setId(Integer.valueOf(attributes.getValue(i)));
            }
        }
        preTag = localName;
    }


    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
        super.endElement(uri, localName, qName);
        Log.i(TAG, "--->" + qName + "---endElement");
        if ("person".equals(localName)) {
            users.add(user);
            user = null;
        }
        /**
         * 防止空节点对characters中条件的影响
         */
        preTag = null;
    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        String data = new String(ch, start, length).trim();
        if (!"".equals(data.trim())) {
            Log.i(TAG, "-->" + preTag + "-->content--" + data.trim());
        }
        if ("name".equals(preTag)) {
            user.setName(data.trim());
        } else if ("age".equals(preTag)) {
            user.setAge(new Short(data.trim()));
        }
    }

    public List<ISUserInfo> getUsersInfo() {
        return users;
    }


}
