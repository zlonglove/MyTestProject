package com.ISHello.DesignMode.prototype;

public class LoginSession {
    private static volatile LoginSession loginSession;
    private User loginUser;
    private LoginSession() {

    }

    public static LoginSession getInstance() {
        if (loginSession == null) {
            synchronized (LoginSession.class) {
                if (loginSession == null) {
                    loginSession = new LoginSession();
                }
            }
        }
        return loginSession;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public User getLoginUser() {
        return loginUser;
    }

    public User getCloneLoginUser() {
        return loginUser.clone();
    }
}
