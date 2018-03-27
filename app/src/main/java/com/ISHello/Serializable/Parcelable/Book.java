package com.ISHello.Serializable.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private String bookName;
    private String author;
    private int publishTime;

    public Book(String bookName, String author, int publishTime) {
        this.bookName = bookName;
        this.author = author;
        this.publishTime = publishTime;
    }

    public Book() {

    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(int publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 反序列化过程,如何序列化对象和数组
     */
    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            Book mBook = new Book();
            mBook.bookName = source.readString();
            mBook.author = source.readString();
            mBook.publishTime = source.readInt();
            return mBook;
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
     * @param flags
     */
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(bookName);
        parcel.writeString(author);
        parcel.writeInt(publishTime);
    }

}
