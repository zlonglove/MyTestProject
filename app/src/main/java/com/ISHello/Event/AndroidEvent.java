package com.ISHello.Event;

import java.io.Serializable;


@SuppressWarnings("serial")
public class AndroidEvent implements Serializable {
    private AndroidEvents doWhat;
    private String parameter;
    private long time;

    public void populate(AndroidEvents _doWhat, String para, long _time) {
        // Copy fields.
        this.doWhat = _doWhat;
        this.time = _time;
        this.parameter = para;
    }

    public String getParam() {
        return parameter;
    }

    public AndroidEvents getDoWhat() {
        return doWhat;
    }


    public long getTime() {
        return time;
    }

}
