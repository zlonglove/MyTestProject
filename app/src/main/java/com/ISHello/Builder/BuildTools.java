package com.ISHello.Builder;

/**
 * Created by zhanglong on 2018/3/21.
 * Command+n Builder
 */

public class BuildTools {
    private final String mToolsName;
    private final String mDownTime;
    private final String mDesc;
    private final String mDevelop;

    private BuildTools(Builder builder) {
        mToolsName = builder.mToolsName;
        mDownTime = builder.mDownTime;
        mDesc = builder.mDesc;
        mDevelop = builder.mDevelop;
    }


    public static final class Builder {
        private String mToolsName;
        private String mDownTime;
        private String mDesc;
        private String mDevelop;

        public Builder() {
        }

        public Builder mToolsName(String mToolsName) {
            this.mToolsName = mToolsName;
            return this;
        }

        public Builder mDownTime(String mDownTime) {
            this.mDownTime = mDownTime;
            return this;
        }

        public Builder mDesc(String mDesc) {
            this.mDesc = mDesc;
            return this;
        }

        public Builder mDevelop(String mDevelop) {
            this.mDevelop = mDevelop;
            return this;
        }

        public BuildTools build() {
            return new BuildTools(this);
        }
    }

    @Override
    public String toString() {
        return "BuildTools{" +
                "mToolsName='" + mToolsName + '\'' +
                ", mDownTime='" + mDownTime + '\'' +
                ", mDesc='" + mDesc + '\'' +
                ", mDevelop='" + mDevelop + '\'' +
                '}';
    }
}
