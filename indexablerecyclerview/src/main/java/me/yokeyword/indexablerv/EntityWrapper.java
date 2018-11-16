package me.yokeyword.indexablerv;

/**
 * @author 每一个item数据结构
 */
public class EntityWrapper<T> {
    /**
     * item显示的是标题[A]
     */
    static final int TYPE_TITLE = 0;
    /**
     * item显示正文
     */
    static final int TYPE_CONTENT = 1;

    static final int TYPE_HEADER = 1;
    static final int TYPE_FOOTER = 2;
    /**
     * A
     */
    private String index;
    /**
     * A
     */
    private String indexTitle;
    /**
     * aomentebiexingzhengqu
     */
    private String pinyin;
    /**
     * 澳门特别行政区
     */
    private String indexByField;
    /**
     * id = 0
     * name = "澳门特别行政区"
     * pinyin = "aomentebiexingzhengqu"
     */
    private T data;
    /**
     * 5
     */
    private int originalPosition = -1;
    /**
     * item类型,标题和内容
     */
    private int itemType = TYPE_CONTENT;
    private int headerFooterType;

    EntityWrapper() {
    }

    EntityWrapper(String index, int itemType) {
        this.index = index;
        this.indexTitle = index;
        this.pinyin = index;
        this.itemType = itemType;
    }

    public String getIndex() {
        return index;
    }

    void setIndex(String index) {
        this.index = index;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    public String getPinyin() {
        return pinyin;
    }

    void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getIndexByField() {
        return indexByField;
    }

    void setIndexByField(String indexByField) {
        this.indexByField = indexByField;
    }

    public T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    int getItemType() {
        return itemType;
    }

    void setItemType(int itemType) {
        this.itemType = itemType;
    }

    int getHeaderFooterType() {
        return headerFooterType;
    }

    void setHeaderFooterType(int headerFooterType) {
        this.headerFooterType = headerFooterType;
    }

    public boolean isTitle() {
        return itemType == TYPE_TITLE;
    }

    public boolean isContent() {
        return itemType == TYPE_CONTENT;
    }

    public boolean isHeader() {
        return headerFooterType == TYPE_HEADER;
    }

    public boolean isFooter() {
        return headerFooterType == TYPE_FOOTER;
    }
}