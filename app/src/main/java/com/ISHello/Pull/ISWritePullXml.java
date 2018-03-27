package com.ISHello.Pull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.ISHello.UserInfo.ISUserInfo;

public class ISWritePullXml {
    public void save(List<ISUserInfo> users, OutputStream outStream) throws IllegalArgumentException, IllegalStateException, IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(outStream, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "persons");
        for (ISUserInfo user : users) {
            serializer.startTag(null, "person");
            serializer.attribute(null, "id", user.getId().toString());

            serializer.startTag(null, "name");
            serializer.text(user.getName());
            serializer.endTag(null, "name");

            serializer.startTag(null, "age");
            serializer.text(user.getAge().toString());
            serializer.endTag(null, "age");

            serializer.endTag(null, "person");
        }
        serializer.endTag(null, "persons");
        serializer.endDocument();
        outStream.flush();
        outStream.close();
    }


}
