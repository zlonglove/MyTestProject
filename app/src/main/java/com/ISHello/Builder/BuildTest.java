package com.ISHello.Builder;

public class BuildTest {
    public void test() {
        BuildTools buildTools=new BuildTools.Builder().mToolsName("toolsName").mDesc("mDesc").mDevelop("mDevelop")
                .build();
    }
}
