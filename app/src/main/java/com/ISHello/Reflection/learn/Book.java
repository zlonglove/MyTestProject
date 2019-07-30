package com.ISHello.Reflection.learn;

public class Book extends Book1 {
    private final static String TAG = Book.class.getSimpleName();
    private String name;
    private String author;

    public Book() {
    }

    /**
     * 私有构造方法
     *
     * @param name
     * @param author
     */
    private Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * private方法
     *
     * @param index
     * @return
     */
    private String declaredMethod(int index) {
        String string;
        switch (index) {
            case 0:
                string = "I am declaredMethod 1 !";
                break;
            case 1:
                string = "I am declaredMethod 2 !";
                break;
            default:
                string = "I am declaredMethod 1 !";
        }

        return string;
    }

    /**
     * public方法
     *
     * @param index
     * @return
     */
    public String Method(int index) {
        String string;
        switch (index) {
            case 0:
                string = "I am declaredMethod 1 !";
                break;
            case 1:
                string = "I am declaredMethod 2 !";
                break;
            default:
                string = "I am declaredMethod 1 !";
        }

        return string;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
