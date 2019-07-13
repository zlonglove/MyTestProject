package com.ISHello.DesignMode.prototype;

public class Main {
    public static void main(String[] args) {
        Login login = new LoginImpl();
        login.login();

        User user = LoginSession.getInstance().getCloneLoginUser();
        user.address=new Address("anhui","huoshan","caijiahe");
        System.out.println(user.toString());

        System.out.println(LoginSession.getInstance().getLoginUser().toString());
    }
}
