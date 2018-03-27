package com.ISHello.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kfzx-zhanglong on 2016/3/14.
 * Company ICBC
 */
public class Book implements Parcelable {
    private int bookId;
    private String bookName;

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    private Book(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    /**
     * 反序列化过程,如何序列化对象和数组
     */
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化过程
     * 将当前对象写入序列化结构中，其中flags有两种(0、1)
     * 1-标识当前对象需要作为返回值返回，不能立即释放资源
     * 0-几乎所有的情况default为0
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bookId);
        parcel.writeString(this.bookName);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
