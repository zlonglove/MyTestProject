package com.ISHello.DesignMode.prototype;

public class LoginImpl implements Login {
    @Override
    public void login() {
        User user = new User();
        user.name = "zhanglong";
        user.age = 22;
        user.phoneNum = "12345674567";
        user.address = new Address("北京市", "海淀区", "于辛庄");
        LoginSession.getInstance().setLoginUser(user);
    }
}
