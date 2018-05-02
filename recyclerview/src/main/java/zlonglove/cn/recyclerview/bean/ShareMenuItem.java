package zlonglove.cn.recyclerview.bean;

/**
 * Created by zhanglong on 2018/4/19.
 */

public class ShareMenuItem {
    private String title;
    private int resourceId;

    public ShareMenuItem(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }
}
