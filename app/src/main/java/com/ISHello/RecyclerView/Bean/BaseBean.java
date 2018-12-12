package com.ISHello.RecyclerView.Bean;

import java.io.Serializable;

/**
 */

public class BaseBean implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * status : 1
     * message : 成功！
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}