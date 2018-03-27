package com.ISHello.Builder;

/**
 * Created by zhanglong on 2017/5/23.
 */

public class User {
    private final String mFirstName;
    private final String mLastName;
    private final String mGender;
    private final String mAge;
    private final String mPhoneNo;

    private User(Builder builder) {
        mFirstName = builder.mFirstName;
        mLastName = builder.mLastName;
        mGender = builder.mGender;
        mAge = builder.mAge;
        mPhoneNo = builder.mPhoneNo;
    }

    public static final class Builder {
        private String mFirstName;
        private String mLastName;
        private String mGender;
        private String mAge;
        private String mPhoneNo;

        public Builder() {
        }

        public Builder mFirstName(String val) {
            mFirstName = val;
            return this;
        }

        public Builder mLastName(String val) {
            mLastName = val;
            return this;
        }

        public Builder mGender(String val) {
            mGender = val;
            return this;
        }

        public Builder mAge(String val) {
            mAge = val;
            return this;
        }

        public Builder mPhoneNo(String val) {
            mPhoneNo = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mAge='" + mAge + '\'' +
                ", mPhoneNo='" + mPhoneNo + '\'' +
                '}';
    }
}
