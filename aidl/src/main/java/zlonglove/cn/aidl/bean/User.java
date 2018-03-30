package zlonglove.cn.aidl.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhanglong on 2018/3/28.
 */

public class User implements Parcelable {
    private long mUserid;
    private byte[] mPassword;
    private byte[] mToken;
    private byte[] mCredential;
    private long phoneNum;
    private String name;
    private String email;


    private String ICBCUserID;//	客户统一通行证id
    private String ICBCloginUserName;//用户名（非实名客户首次登陆时返回）
    private String UserType;//	UserType：客户类型：1-实名 2-非实名 3-未注册

    /**
     * @return the userType
     */
    public String getUserType() {
        return UserType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        UserType = userType;
    }

    /**
     * @return the iCBCloginUserName
     */
    public String getICBCloginUserName() {
        return ICBCloginUserName;
    }

    /**
     * @param iCBCloginUserName the iCBCloginUserName to set
     */
    public void setICBCloginUserName(String iCBCloginUserName) {
        ICBCloginUserName = iCBCloginUserName;
    }

    private String CMPPWD;


    /**
     * @return the cMPPWD
     */
    public String getCMPPWD() {
        return CMPPWD;
    }

    /**
     * @param cMPPWD the cMPPWD to set
     */
    public void setCMPPWD(String cMPPWD) {
        CMPPWD = cMPPWD;
    }

    /**
     * @return the iCBCUserID
     */
    public String getICBCUserID() {
        return ICBCUserID;
    }

    /**
     * @param iCBCUserID the iCBCUserID to set
     */
    public void setICBCUserID(String iCBCUserID) {
        ICBCUserID = iCBCUserID;
    }

    public User(long userid, byte[] password, byte[] token) {
        mUserid = userid;
        mPassword = password;
        mToken = token;
    }

    public User() {
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUserID(long userId) {
        this.mUserid = userId;
    }

    public long getUserId() {
        return mUserid;
    }

    public byte[] getCredential() {
        return mCredential;
    }

    public void setCredential(byte[] credential) {
        mCredential = credential;
    }

    public void setToken(byte[] token) {
        this.mToken = token;
    }

    public byte[] getToken() {
        return this.mToken;
    }

    public void setPassword(byte[] password) {
        this.mPassword = password;
    }

    public byte[] getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mUserid);
        if (mPassword != null) {
            out.writeInt(mPassword.length);
            out.writeByteArray(mPassword);
        } else {
            out.writeInt(-1);
        }
        if (mToken != null) {
            out.writeInt(mToken.length);
            out.writeByteArray(mToken);
        } else {
            out.writeInt(-1);
        }
        if (mCredential != null) {
            out.writeInt(mCredential.length);
            out.writeByteArray(mCredential);
        } else {
            out.writeInt(-1);
        }
        out.writeLong(phoneNum);
        out.writeString(name);
        out.writeString(email);
        out.writeString(ICBCUserID);
        out.writeString(ICBCloginUserName);
        out.writeString(UserType);
        out.writeString(CMPPWD);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        mUserid = in.readLong();
        int length = in.readInt();
        if (length >= 0) {
            mPassword = new byte[length];
            in.readByteArray(mPassword);
        }
        length = in.readInt();
        if (length >= 0) {
            mToken = new byte[length];
            in.readByteArray(mToken);
        }
        length = in.readInt();
        if (length >= 0) {
            mCredential = new byte[length];
            in.readByteArray(mCredential);
        }
        phoneNum = in.readLong();
        name = in.readString();
        email = in.readString();
        ICBCUserID = in.readString();
        ICBCloginUserName = in.readString();
        UserType = in.readString();
        CMPPWD = in.readString();
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserid=" + mUserid +
                ", mPassword=" + new String(mPassword) +
                ", mToken=" + new String(mToken) +
                ", mCredential=" + new String(mCredential) +
                ", phoneNum=" + phoneNum +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", ICBCUserID='" + ICBCUserID + '\'' +
                ", ICBCloginUserName='" + ICBCloginUserName + '\'' +
                ", UserType='" + UserType + '\'' +
                ", CMPPWD='" + CMPPWD + '\'' +
                '}';
    }
}
