package me.yokeyword.indexablerv;

/**
 * @author
 */
public interface IndexableEntity {

    String getFieldIndexBy();

    void setFieldIndexBy(String indexField);

    void setFieldPinyinIndexBy(String pinyin);
}
